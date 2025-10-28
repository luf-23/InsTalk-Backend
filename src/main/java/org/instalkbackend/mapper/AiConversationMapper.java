package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.instalkbackend.model.po.AiConversation;

import java.util.List;

@Mapper
public interface AiConversationMapper {

    @Select("SELECT * FROM ai_conversation WHERE user_id = #{userId} AND robot_id = #{robotId}")
    List<AiConversation> selectByUserIdAndRobotId(Long userId, Long robotId);

    @Select("SELECT robot_id FROM ai_conversation WHERE id = #{conversationId}")
    Long selectRobotIdById(Long conversationId);

    @Select("SELECT * FROM ai_conversation WHERE id = #{conversationId}")
    AiConversation selectById(Long conversationId);

    @Insert("INSERT INTO ai_conversation (robot_id, user_id) VALUES (#{robotId}, #{userId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void add(AiConversation aiConversation);


}
