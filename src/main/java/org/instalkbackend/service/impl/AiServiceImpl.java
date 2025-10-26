package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.UserAiConfigMapper;
import org.instalkbackend.model.dto.AiChatDTO;
import org.instalkbackend.model.dto.UserAiConfigDTO;
import org.instalkbackend.model.po.UserAiConfig;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AiService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class AiServiceImpl implements AiService {

    private final Map<Long, Set<String>> userTasksMap = new HashMap<>();
    @Autowired
    private WebClient aiWebClient;
    @Autowired
    private UserAiConfigMapper userAiConfigMapper;

    @Override
    public Result<String> getCredential() {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        Long userId = ThreadLocalUtil.getId();
        if (userTasksMap.containsKey(userId)){
            userTasksMap.get(userId).add(taskId);
        }else{
            userTasksMap.put(userId, Set.of(taskId));
        }
        return Result.success(taskId);
    }

    @Override
    public SseEmitter streamChat(AiChatDTO aiChatDTO) {
        
    }

    @Override
    public Result update(UserAiConfigDTO userAiConfigDTO) {
        Long userId = ThreadLocalUtil.getId();
        UserAiConfig userAiConfig = userAiConfigMapper.select(userId, userAiConfigDTO.getRobotId());
        UserAiConfig newUserAiConfig = new UserAiConfig(userAiConfig, userAiConfigDTO);
        userAiConfigMapper.update(newUserAiConfig);
        return Result.success();
    }


}
