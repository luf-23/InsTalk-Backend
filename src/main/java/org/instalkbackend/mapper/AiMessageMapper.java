package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.instalkbackend.model.po.AiMessage;

import java.util.List;

@Mapper
public interface AiMessageMapper {

    @Select("select * from ai_message where conversation_id = #{conversationId}")
    List<AiMessage> select(Long conversationId);

    @Insert("insert into ai_message (conversation_id, role, content) values (#{conversationId}, #{role}, #{content})")
    void add(AiMessage aiMessage);
}
