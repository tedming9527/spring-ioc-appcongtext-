package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.management.PlatformManagedObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
@Transactional
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
          );
        }
      }
    });
  }
  public User getUserByEmail(String email) {
    return jdbcTemplate.queryForObject("Select * from users where email = ?", (ResultSet rs, int rowNum) -> {
      return new User(
          rs.getLong("id"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("name")
      );
    }, email);
  }
  public List<User> getUsers(int pageIndex) {
    int limit = 100;
    int offset = limit * (pageIndex - 1);
    return jdbcTemplate.query("Select * from users limit ? offset ?", new BeanPropertyRowMapper<User>(User.class), limit, offset);
  }
  public void updateUser(User user) {
    if (1 != jdbcTemplate.update("update users set name = ? where id = ?", user.getName(), user.getId())) {
      throw new RuntimeException("User not found by id");
    }
  }

  @Transactional
  public User register(String email, String password, String name) {
    KeyHolder holder = new GeneratedKeyHolder();
    if (1 != jdbcTemplate.update(conn -> {
      var ps = conn.prepareStatement("Insert into users(email, password, name) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      ps.setObject(1, email);
      ps.setObject(2, password);
      ps.setObject(3, name);
      return  ps;
    })) {
      throw new RuntimeException("Insert failed.");
    }
    return  new User(holder.getKey().longValue(), email, password, name);
  }
}
