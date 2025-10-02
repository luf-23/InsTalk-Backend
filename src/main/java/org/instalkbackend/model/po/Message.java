package org.instalkbackend.model.po;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Message {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long groupId;
    private String content;
    private String messageType;
    private LocalTime sentAt;
}
