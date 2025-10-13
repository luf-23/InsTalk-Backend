package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.*;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.FriendVO;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(String email);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    @Insert("insert into user (username,email,password) values (#{username},#{email},#{password})")
    void add(User user);

    @Select({"<script>",
            "SELECT * FROM user ",
            "<where>",
            "   <if test='list != null and list.size() > 0'>",
            "       id IN ",
            "       <foreach collection='list' item='id' open='(' separator=',' close=')'>",
            "           #{id}",
            "       </foreach>",
            "   </if>",
            "   <if test='list == null or list.size() == 0'>",
            "       1 = 0",
            "   </if>",
            "</where>",
            "</script>"})
    List<User> selectByIds(@Param("list") List<Long> ids);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%',#{username},'%')")
    List<User> selectByUsernameLike(String username);

    @Update("update user set username = #{username},email = #{email},password = #{password},signature = #{signature},avatar = #{avatar} where id = #{id}")
    void update(User newUser);
}
