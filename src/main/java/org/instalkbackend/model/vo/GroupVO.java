package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupVO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
    private List<Member> members;
    @Data
    public static class Member{
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
    }
}
