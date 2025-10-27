package org.instalkbackend.model.dto;

import lombok.Data;

@Data
public class AiChatDTO {
    private String taskId;
    private Long conversationId;
    private String message;
}
