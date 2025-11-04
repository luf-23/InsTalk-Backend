package org.instalkbackend.model.vo;

import lombok.Data;
import org.instalkbackend.model.po.User;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String signature;
    private String avatar;

    public UserInfoVO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.signature = user.getSignature();
        this.avatar = user.getAvatar();
    }
}
