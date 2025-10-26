package org.instalkbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiChatDTO {
    private String taskId;
    private Long robotId;
    private List< Message> messages;
    @Data
    public static class Message{
        private String role;
        private String content;
    }
}
