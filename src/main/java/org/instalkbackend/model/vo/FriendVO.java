package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendVO {
    private Long id;
    private String username;
    private String signature;
    private String avatar;
    private LocalDateTime createdAt;
}
