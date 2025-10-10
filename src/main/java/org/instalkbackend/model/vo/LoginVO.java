package org.instalkbackend.model.vo;

import lombok.Data;
import org.instalkbackend.model.po.User;

import java.time.LocalDateTime;

//auth/login
@Data
public class LoginVO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private String role;
    private LocalDateTime createdAt;
    private String accessToken;
    private String refreshToken;

    public LoginVO(User user,String accessToken,String refreshToken){
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
