package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiMessageVO {
    private Long id;
    private String role;//USER or ASSISTANT
    private String content;
    private LocalDateTime sentAt;
}
