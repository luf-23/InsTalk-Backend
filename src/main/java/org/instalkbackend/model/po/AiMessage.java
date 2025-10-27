package org.instalkbackend.model.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiMessage {
    private Long id;
    private Long conversationId;
    private String role;//USER or ASSISTANT
    private String content;
    private LocalDateTime sentAt;
}
