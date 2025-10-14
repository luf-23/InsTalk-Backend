package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageStatusMapper {

    @Insert("insert into message_status (message_id,user_id) values (#{messageId},#{receiverId})")
    void add(Long messageId, Long receiverId);

    @Update("update message_status set is_read = TRUE where user_id = #{receiverId} and message_id = #{messageId}")
    void updateToRead(Long messageId, Long receiverId);

    @Select("select is_read from message_status where message_id=#{messageId} and user_id=#{receiverId}")
    Boolean select(Long messageId, Long receiverId);
}
