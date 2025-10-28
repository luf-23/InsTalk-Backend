package org.instalkbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiChatDTO {
    private String taskId;
    private Long conversationId;
    private String currentUserMessage;
    private List<AiChatMessage> messageHistory;

    @Data
    public static class AiChatMessage{
        private String role;//USER or ASSISTANT
        private String content;
    }
}
