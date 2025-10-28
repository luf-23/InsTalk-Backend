package org.instalkbackend.service;

import org.instalkbackend.model.dto.AiChatDTO;
import org.instalkbackend.model.dto.UserAiConfigDTO;
import org.instalkbackend.model.vo.AiConversationVO;
import org.instalkbackend.model.vo.AiMessageVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserAiConfigVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AiService {
    Result<String> getCredential();

    SseEmitter streamChat(AiChatDTO aiChatDTO);

    Result<Void> update(UserAiConfigDTO userAiConfigDTO);

    Result<List<AiConversationVO>> getConversationList(Long robotId);

    Result<List<AiMessageVO>> getMessageList(Long conversationId);

    Result<UserAiConfigVO> getAiConfig(Long robotId);

    Result<AiConversationVO> createConversation(Long robotId, Long userId);
}
