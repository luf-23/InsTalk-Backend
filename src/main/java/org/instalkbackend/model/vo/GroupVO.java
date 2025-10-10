package org.instalkbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupVO {
    private Long id;
    private String name;
    private String description;
    private String avatar;
    private Long ownerId;
    private LocalDateTime createdAt;
    private List<Long> adminIds;
    private List<Member> members;
    @Data
    public static class Member{
        private Long id;
        private String username;
        private String signature;
        private String avatar;
    }
}
