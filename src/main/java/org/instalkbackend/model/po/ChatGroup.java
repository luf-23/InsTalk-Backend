package org.instalkbackend.model.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatGroup {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
}
