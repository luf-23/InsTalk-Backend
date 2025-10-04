package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long groupId;
    private String content;
    private String messageType;
    private LocalDateTime sendAt;
    private Boolean isRead;
    //1.senderId为自己
    //2.receiverId为自己
    //3.groupId群成员有自己且senderId不为自己
}
