package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.*;
import org.instalkbackend.model.po.Message;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("select * from message where id = #{id}")
    Message selectById(Long id);

    @Insert("insert into message (sender_id,receiver_id,content,message_type) values (#{senderId},#{receiverId},#{content},#{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addPrivateMessage(Message message);

    @Insert("insert into message (sender_id,group_id,content,message_type) values (#{senderId},#{groupId},#{content},#{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addGroupMessage(Message message);

    @Select("select * from message where sender_id = #{senderId}")
    List<Message> selectBySenderId(Long senderId);

    @Select("select * from message where receiver_id = #{receiverId}")
    List<Message> selectByReceiverId(Long receiverId);

    @Select("select * from message where receiver_id = #{userId} and id > #{messageId}")
    List<Message> selectNewByReceiverId(Long userId, Long messageId);

    @Select("select * from message where sender_id != #{userId} and group_id in (select group_id from group_member where user_id=#{userId} )")
    List<Message> selectGroupMessagesAsReceiver(Long userId);

    @Select("select * from message where sender_id != #{userId} and group_id in (select group_id from group_member where user_id=#{userId}) and id > #{messageId}")
    List<Message> selectNewGroupMessagesAsReceiver(Long userId,Long messageId);

    @Select("select sent_at from message where id = #{id}")
    LocalDateTime selectSentAtById(Long id);

    @Select({
            "<script>",
            "SELECT * FROM message WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Message> selectByIds(@Param("ids") List<Long> ids);

}
