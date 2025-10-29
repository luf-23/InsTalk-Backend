package org.instalkbackend.service;

import org.instalkbackend.model.dto.AiChatDTO;
import org.instalkbackend.model.dto.UserAiConfigDTO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserAiConfigVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AiService {
    Result<String> getCredential();

    SseEmitter streamChat(AiChatDTO aiChatDTO);

    Result<Void> update(UserAiConfigDTO userAiConfigDTO);

    Result<UserAiConfigVO> getAiConfig(Long robotId);

}
