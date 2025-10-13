package org.instalkbackend.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String signature;
    private String avatar;
    private String email;
}
