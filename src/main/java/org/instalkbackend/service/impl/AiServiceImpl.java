package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.AiConversationMapper;
import org.instalkbackend.mapper.AiMessageMapper;
import org.instalkbackend.mapper.UserAiConfigMapper;
import org.instalkbackend.model.dto.AiChatDTO;
import org.instalkbackend.model.dto.UserAiConfigDTO;
import org.instalkbackend.model.po.AiMessage;
import org.instalkbackend.model.po.UserAiConfig;
import org.instalkbackend.model.vo.AiConversationVO;
import org.instalkbackend.model.vo.AiMessageVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserAiConfigVO;
import org.instalkbackend.service.AiService;
import org.instalkbackend.util.AiUtil;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

@Service
public class AiServiceImpl implements AiService {

    private final Map<Long, Set<String>> userTasksMap = new HashMap<>();
    @Autowired
    private WebClient aiWebClient;
    @Autowired
    private AiUtil aiUtil;
    @Autowired
    private UserAiConfigMapper userAiConfigMapper;
    @Autowired
    private AiConversationMapper aiConversationMapper;
    @Autowired
    private AiMessageMapper aiMessageMapper;

    @Override
    public Result<String> getCredential() {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        Long userId = ThreadLocalUtil.getId();
        if (userTasksMap.containsKey(userId)){
            userTasksMap.get(userId).add(taskId);
        }else{
            Set<String> taskSet = new HashSet<>();
            taskSet.add(taskId);
            userTasksMap.put(userId, taskSet);
        }
        return Result.success(taskId);
    }

    @Override
    public SseEmitter streamChat(AiChatDTO aiChatDTO) {
        Long userId = ThreadLocalUtil.getId();
        
        // 验证taskId
        String taskId = aiChatDTO.getTaskId();
        if (taskId == null || !userTasksMap.containsKey(userId) || !userTasksMap.get(userId).contains(taskId)) {
            throw new RuntimeException("无效的任务ID");
        }
        
        // 验证conversationId和获取配置
        Long conversationId = aiChatDTO.getConversationId();
        Long robotId = aiConversationMapper.selectRobotIdById(conversationId);
        if (robotId == null) {
            throw new RuntimeException("对话不存在");
        }
        
        // 获取用户AI配置
        UserAiConfig userAiConfig = userAiConfigMapper.select(userId, robotId);
        if (userAiConfig == null) {
            throw new RuntimeException("AI配置不存在");
        }

        if (aiUtil.needsReset(userAiConfig)){
            userAiConfigMapper.resetMessageCount(userId, robotId);
        }

        // 检查消息限制
        if (!aiUtil.canSendMessage(userAiConfig)) {
            throw new RuntimeException("已达到每日消息限制");
        }

        // 创建SSE发射器，设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(300000L);
        
        // 获取历史消息
        List<AiMessage> historyMessages = aiMessageMapper.select(conversationId);
        
        // 保存用户消息
        AiMessage userMessage = new AiMessage();
        userMessage.setConversationId(conversationId);
        userMessage.setRole("USER");
        userMessage.setContent(aiChatDTO.getMessage());
        aiMessageMapper.add(userMessage);
        
        // 构建请求体
        String requestBody = aiUtil.buildRequestBody(historyMessages, userAiConfig, aiChatDTO.getMessage());
        
        // 用于累积AI的完整回复
        StringBuilder fullResponse = new StringBuilder();
        
        // 异步调用AI接口
        aiWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(chunk -> {
                    try {
                        // 解析流式响应
                        String content = aiUtil.parseStreamResponse(chunk);
                        if (content != null && !content.isEmpty()) {
                            fullResponse.append(content);
                            // 发送到前端
                            emitter.send(SseEmitter.event()
                                    .data(content)
                                    .name("message"));
                        }
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .doOnComplete(() -> {
                    try {
                        // 保存AI回复
                        AiMessage assistantMessage = new AiMessage();
                        assistantMessage.setConversationId(conversationId);
                        assistantMessage.setRole("ASSISTANT");
                        assistantMessage.setContent(fullResponse.toString());
                        aiMessageMapper.add(assistantMessage);
                        userAiConfigMapper.increaseMessageCount(userId, robotId);
                        
                        // 发送完成信号
                        emitter.send(SseEmitter.event()
                                .data("[DONE]")
                                .name("done"));
                        emitter.complete();
                        
                        // 清理taskId
                        if (userTasksMap.containsKey(userId)) {
                            userTasksMap.get(userId).remove(taskId);
                            if (userTasksMap.get(userId).isEmpty()) {
                                userTasksMap.remove(userId);
                            }
                        }
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .doOnError(error -> {
                    emitter.completeWithError(error);
                    // 清理taskId
                    if (userTasksMap.containsKey(userId)) {
                        userTasksMap.get(userId).remove(taskId);
                        if (userTasksMap.get(userId).isEmpty()) {
                            userTasksMap.remove(userId);
                        }
                    }
                })
                .subscribe();
        
        // 设置SSE发射器的超时和完成回调
        emitter.onTimeout(() -> {
            emitter.complete();
            // 清理taskId
            if (userTasksMap.containsKey(userId)) {
                userTasksMap.get(userId).remove(taskId);
                if (userTasksMap.get(userId).isEmpty()) {
                    userTasksMap.remove(userId);
                }
            }
        });
        
        emitter.onCompletion(() -> {
            // 清理taskId
            if (userTasksMap.containsKey(userId)) {
                userTasksMap.get(userId).remove(taskId);
                if (userTasksMap.get(userId).isEmpty()) {
                    userTasksMap.remove(userId);
                }
            }
        });
        
        return emitter;
    }

    @Override
    public Result<Void> update(UserAiConfigDTO userAiConfigDTO) {
        Long userId = ThreadLocalUtil.getId();
        UserAiConfig userAiConfig = userAiConfigMapper.select(userId, userAiConfigDTO.getRobotId());
        UserAiConfig newUserAiConfig = new UserAiConfig(userAiConfig, userAiConfigDTO);
        userAiConfigMapper.update(newUserAiConfig);
        return Result.success(null);
    }

    @Override
    public Result<List<AiConversationVO>> getConversationList(Long robotId) {
        Long userId = ThreadLocalUtil.getId();
        return Result.success(aiConversationMapper.selectByUserIdAndRobotId(userId, robotId).stream().map(aiConversation -> {
            AiConversationVO aiConversationVO = new AiConversationVO();
            aiConversationVO.setId(aiConversation.getId());
            aiConversationVO.setRobotId(aiConversation.getRobotId());
            aiConversationVO.setTitle(aiConversation.getTitle());
            aiConversationVO.setSummary(aiConversation.getSummary());
            aiConversationVO.setLastMessageAt(aiConversation.getLastMessageAt());
            aiConversationVO.setCreatedAt(aiConversation.getCreatedAt());
            return aiConversationVO;
        }).toList());
    }

    @Override
    public Result<List<AiMessageVO>> getMessageList(Long conversationId) {
        return Result.success(aiMessageMapper.select(conversationId).stream().map(aiMessage -> {
            AiMessageVO aiMessageVO = new AiMessageVO();
            aiMessageVO.setId(aiMessage.getId());
            aiMessageVO.setRole(aiMessage.getRole());
            aiMessageVO.setContent(aiMessage.getContent());
            aiMessageVO.setSentAt(aiMessage.getSentAt());
            return aiMessageVO;
        }).toList());
    }

    @Override
    public Result<UserAiConfigVO> getAiConfig(Long robotId) {
        Long userId = ThreadLocalUtil.getId();
        UserAiConfig userAiConfig = userAiConfigMapper.select(userId, robotId);
        UserAiConfigVO userAiConfigVO = new UserAiConfigVO();
        userAiConfigVO.setSystemPrompt(userAiConfig.getSystemPrompt());
        userAiConfigVO.setModel(userAiConfig.getModel());
        userAiConfigVO.setTemperature(userAiConfig.getTemperature());
        userAiConfigVO.setMaxTokens(userAiConfig.getMaxTokens());
        userAiConfigVO.setTopP(userAiConfig.getTopP());
        userAiConfigVO.setPresencePenalty(userAiConfig.getPresencePenalty());
        userAiConfigVO.setSeed(userAiConfig.getSeed());
        userAiConfigVO.setDailyMessageLimit(userAiConfig.getDailyMessageLimit());
        userAiConfigVO.setDailyMessageCount(userAiConfig.getDailyMessageCount());
        userAiConfigVO.setLastResetDate(userAiConfig.getLastResetDate());
        userAiConfigVO.setTotalMessages(userAiConfig.getTotalMessages());
        userAiConfigVO.setTotalTokensUsed(userAiConfig.getTotalTokensUsed());
        userAiConfigVO.setLastUsedAt(userAiConfig.getLastUsedAt());
        return Result.success(userAiConfigVO);
    }


}
