package org.instalkbackend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.instalkbackend.model.po.AiMessage;
import org.instalkbackend.model.po.UserAiConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构建AI请求体
     * @param messages 消息历史列表
     * @param userAiConfig 用户AI配置
     * @return 请求体JSON字符串
     */
    public String buildRequestBody(List<AiMessage> messages, UserAiConfig userAiConfig, String userMessage) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            
            // 设置模型
            requestBody.put("model", userAiConfig.getModel() != null ? userAiConfig.getModel() : "qwen-plus");
            
            // 构建消息数组
            ArrayNode messagesArray = objectMapper.createArrayNode();
            
            // 添加系统提示词
            if (userAiConfig.getSystemPrompt() != null && !userAiConfig.getSystemPrompt().isEmpty()) {
                ObjectNode systemMessage = objectMapper.createObjectNode();
                systemMessage.put("role", "system");
                systemMessage.put("content", userAiConfig.getSystemPrompt());
                messagesArray.add(systemMessage);
            }
            
            // 添加历史消息
            if (messages != null && !messages.isEmpty()) {
                for (AiMessage message : messages) {
                    ObjectNode msgNode = objectMapper.createObjectNode();
                    msgNode.put("role", message.getRole().toLowerCase());
                    msgNode.put("content", message.getContent());
                    messagesArray.add(msgNode);
                }
            }
            
            // 添加用户当前消息
            ObjectNode userMsg = objectMapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messagesArray.add(userMsg);
            
            requestBody.set("messages", messagesArray);
            
            // 设置参数
            if (userAiConfig.getTemperature() != null) {
                requestBody.put("temperature", userAiConfig.getTemperature());
            }
            if (userAiConfig.getTopP() != null) {
                requestBody.put("top_p", userAiConfig.getTopP());
            }
            if (userAiConfig.getMaxTokens() != null) {
                requestBody.put("max_tokens", userAiConfig.getMaxTokens());
            }
            if (userAiConfig.getPresencePenalty() != null) {
                requestBody.put("presence_penalty", userAiConfig.getPresencePenalty());
            }
            if (userAiConfig.getSeed() != null) {
                requestBody.put("seed", userAiConfig.getSeed());
            }
            
            // 开启流式输出
            requestBody.put("stream", true);
            
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("构建请求体失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析流式响应数据
     * @param line SSE数据行
     * @return 提取的内容，如果没有内容则返回null
     */
    public String parseStreamResponse(String line) {
        try {
            // 跳过空行和非data行
            if (line == null || line.trim().isEmpty() || !line.startsWith("data:")) {
                return null;
            }
            
            // 去除"data: "前缀
            String jsonData = line.substring(5).trim();
            
            // 检查是否是结束标记
            if ("[DONE]".equals(jsonData)) {
                return null;
            }
            
            // 解析JSON
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode choicesNode = rootNode.path("choices");
            
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode deltaNode = firstChoice.path("delta");
                JsonNode contentNode = deltaNode.path("content");
                
                if (!contentNode.isMissingNode() && contentNode.isTextual()) {
                    return contentNode.asText();
                }
            }
            
            return null;
        } catch (Exception e) {
            // 解析失败时返回null，不中断流
            return null;
        }
    }

    /**
     * 验证用户是否有发送消息的权限
     * @param userAiConfig 用户AI配置
     * @return 是否有权限
     */
    public boolean canSendMessage(UserAiConfig userAiConfig) {
        if (userAiConfig.getDailyMessageLimit() == null) {
            return true; // 无限制
        }
        
        return userAiConfig.getDailyMessageCount() < userAiConfig.getDailyMessageLimit();
    }

    /**
     * 生成会话标题
     * @param firstMessage 第一条用户消息
     * @return 会话标题
     */
    public String generateConversationTitle(String firstMessage) {
        if (firstMessage == null || firstMessage.isEmpty()) {
            return "新对话";
        }
        
        // 截取前20个字符作为标题
        if (firstMessage.length() <= 20) {
            return firstMessage;
        }
        
        return firstMessage.substring(0, 20) + "...";
    }
}
