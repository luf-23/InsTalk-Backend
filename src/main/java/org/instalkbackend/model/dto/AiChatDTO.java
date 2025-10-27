package org.instalkbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiChatDTO {
    private String taskId;
    private Long conversationId;
    private String message;
}
