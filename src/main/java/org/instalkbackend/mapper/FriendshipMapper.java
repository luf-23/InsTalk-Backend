package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.*;
import org.instalkbackend.model.po.Friendship;

@Mapper
public interface FriendshipMapper {

    @Select("SELECT * FROM friendship WHERE user_id1 = #{id1} AND user_id2 = #{id2}")
    Friendship selectByUserId1AndUserId2(Long id1, Long id2);

    @Insert("insert into friendship (user_id1,user_id2) values (#{id1},#{id2})")
    void addRequest(Long id1, Long id2);

    @Update("update friendship set status = 'ACCEPTED' WHERE user_id1= #{id1} AND user_id2= #{id2} AND status = 'PENDING'")
    void acceptRequest(Long id1, Long id2);

    @Delete("delete from friendship where user_id1= #{id1} AND user_id2= #{id2} AND status='PENDING'")
    void rejectRequest(Long id1, Long id2);

    @Delete("delete from friendship where user_id1= #{id1} AND user_id2= #{id2} AND status='ACCEPTED'")
    void deleteRequest(Long id1, Long id2);
}
