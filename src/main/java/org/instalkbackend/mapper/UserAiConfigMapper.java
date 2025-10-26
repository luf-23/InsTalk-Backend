package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.instalkbackend.model.po.UserAiConfig;

@Mapper
public interface UserAiConfigMapper {

    @Insert("insert into user_ai_config (user_id,robot_id) values (#{userId},#{robotId})")
    void add(Long userId, Long robotId);


    @Select("SELECT * FROM user_ai_config WHERE user_id = #{userId} AND robot_id = #{robotId}")
    UserAiConfig select(Long userId, Long robotId);

    @Update("UPDATE user_ai_config SET system_prompt=#{systemPrompt},model=#{model},temperature=#{temperature},top_p=#{topP},presence_penalty=#{presencePenalty},seed=#{seed} WHERE user_id=#{userId} AND robot_id=#{robotId}")
    void update(UserAiConfig newUserAiConfig);
}
