package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageStatusMapper {

    @Insert("insert into message_status (message_id,user_id) values (#{messageId},#{userId})")
    void add(Long messageId, Long userId);

    @Update("update message_status set is_read = TRUE where user_id = #{userId} and message_id = #{messageId}")
    void updateToRead(Long messageId, Long userId);

    @Select("select is_read from message_status where message_id=#{messageId} and user_id=#{userId}")
    Boolean select(Long messageId, Long userId);
}
