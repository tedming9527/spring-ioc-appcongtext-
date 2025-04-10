package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class UserService {
  @Autowired
  JdbcTemplate jdbcTemplate;

  public User getUserById(long id) {
    return jdbcTemplate.execute((Connection conn) -> {
      try (var ps = conn.prepareStatement("Select * from  users where id = ?")) {
        ps.setObject(1, id);
        try(var rs = ps.executeQuery()) {
          if (rs.next()) {
            return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name")
            );
          }
          throw new RuntimeException("user not found by id");
        }
      }
    });
  }
  public User getUserByName(String name) {
    return jdbcTemplate.execute("select * from users where name = ?", (PreparedStatement ps) -> {
      ps.setObject(1, name);
      try(var rs = ps.executeQuery()) {
        if (rs.next()) {
          return new User(
              rs.getLong("id"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("name")
          )
        }
      }
    })
  }
}
