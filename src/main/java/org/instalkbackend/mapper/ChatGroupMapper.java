package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.instalkbackend.model.dto.GroupDTO;
import org.instalkbackend.model.po.ChatGroup;

import java.util.List;

@Mapper
public interface ChatGroupMapper {

    @Insert(("insert into chat_group (owner_id,name,description) values (#{ownerId},#{groupDTO.name},#{groupDTO.description})"))
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long add(Long ownerId, GroupDTO groupDTO);

    @Select("SELECT * FROM chat_group WHERE owner_id = #{userId}")
    List<ChatGroup> selectByOwnerId(Long userId);

    @Select("SELECT * FROM chat_group WHERE id = #{groupId}")
    ChatGroup selectById(Long groupId);
}
