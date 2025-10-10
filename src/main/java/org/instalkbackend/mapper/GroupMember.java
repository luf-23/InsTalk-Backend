package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.instalkbackend.model.vo.GroupVO;

import java.util.List;

@Mapper
public interface GroupMember {

    @Insert("insert into group_member (user_id,group_id,role) values (#{ownerId},#{groupId},'OWNER')")
    void addOwner(Long ownerId, Long groupId);

    @Select("SELECT * FROM group_member WHERE user_id = #{userId} AND group_id = #{groupId}")
    GroupMember select(Long userId, Long groupId);

    @Insert("insert into group_member (user_id,group_id) values (#{userId},#{groupId})")
    void addMember(Long userId, Long groupId);

    @Select("SELECT id,username,signature,avatar FROM user WHERE id IN (SELECT user_id FROM group_member WHERE group_id = #{id})")
    List<GroupVO.Member> selectMembersByGroupId(Long id);

    @Select("SELECT group_id FROM group_member WHERE user_id=#{userId}")
    List<Long> selectGroupIdIfIam(Long userId);

    @Select("SELECT user_id FROM group_member WHERE group_id = #{id} AND role = 'ADMIN'")
    List<Long> selectAdminIdsByGroupId(Long id);
}
