package org.instalkbackend.model.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String username,String email,String password){
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
