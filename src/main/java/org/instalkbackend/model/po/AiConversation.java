package org.instalkbackend.model.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiConversation {
    private Long id;
    private Long robotId;
    private Long userId;
    private String title;
    private String summary;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}
