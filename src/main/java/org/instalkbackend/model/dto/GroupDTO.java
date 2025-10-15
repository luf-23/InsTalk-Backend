package org.instalkbackend.model.dto;

import lombok.Data;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String avatar;
}
