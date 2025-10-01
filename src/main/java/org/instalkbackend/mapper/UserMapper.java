package org.instalkbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.instalkbackend.model.po.User;

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
}
