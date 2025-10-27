package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiConversationVO {
    private Long id;
    private Long robotId;
    private String title;
    private String summary;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}
