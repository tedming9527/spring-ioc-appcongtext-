package com.itranswarp.learnjava.mapper;

import com.itranswarp.learnjava.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
  @Select("select * from users where id = #{id}")
  User getById(@Param("id") long id);

  @Select("select *  from users limit #{offset}, #{maxResults}")
  List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  @Insert("insert into users (email, password, name createAt) values (#{user.email}, #{user.password}, #{user.password}, #{user.createAt})")
  void insert(@Param("user") User user);

}
