package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.*;

@Component
public class UserService {
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired
  private MailService mailService;
  public UserService() {
    register("anna", "anna@example.com", "password");
    register("alice", "alice@example.com", "password");
    register( "bob", "bob@example.com", "password");
  }

  @Logging(value = "login")
  public void login(String name, String password) {
    String sql = "SELECT * FROM users WHERE name = :name AND password = :password";
    Map<String, Object> params = Map.of("name", name, "password", password);

    try {
      User currentUser = namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
          new User(
              rs.getLong("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password")
          ));

      if (currentUser != null) {
        mailService.sendLoginEmail(currentUser);
      }
    } catch (EmptyResultDataAccessException e) {
      throw new RuntimeException("Invalid username or password");
    }
  }

  public void register(String name, String email, String password) {

    String sql = "INSERT INTO users (name, email, password) VALUES (:name, :email, :password)";
    HashMap<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("email", email);
    params.put("password", password);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    try {
      namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);

      User user = new User(keyHolder.getKey().longValue(), name, email, password); // ID will be autogenerated by the database
      mailService.sendRegisterEmail(user);
    } catch (Exception e) {
      // throw new RuntimeException("User already exists");
    }
  }

  public User getUserById(long id) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("id", id);
    return namedParameterJdbcTemplate.queryForObject("select * from users where name = :id", params, (rs, rowNum) -> {
      if (rs.next()) {
        return new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
        );
      } else {
        throw new EmptyResultDataAccessException("User not found for id: " + id, 1);
      }
    });
  }
  public User getUserByName(String name) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("name", name);
    return namedParameterJdbcTemplate.queryForObject("select * from users where name = :name", params, (rs, rowNum) -> {
        if (rs.next()) {
          return new User(
              rs.getLong("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password")
          );
        } else {
          throw new EmptyResultDataAccessException("User not found for name: " + name, 1);
        }
    });
  }
  public User getUserByEmail(String email) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("email", email);
    return namedParameterJdbcTemplate.queryForObject("select * from users where email = :email", params, (rs, rowNum) -> {
        if (rs.next()) {
          return new User(
              rs.getLong("id"),
              rs.getString("name"),
              rs.getString("email"),
              rs.getString("password")
          );
        } else {
          throw new EmptyResultDataAccessException("User not found for email: " + email, 1);
        }
    });
  }

  public List<User> getUsers(int pageIndex) {
    HashMap<String, Object> params = new HashMap<>();
    int pageSize = 100;
    params.put("limit", pageSize);
    params.put("offset", pageSize * pageIndex);
    return namedParameterJdbcTemplate.query("select * from users limit :limit offset :offset", params, (rs, rowNum) ->
        new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
        )
    );
  }


  public void updateUser(User user) {
    String sql = "UPDATE users SET name = :name, email = :email, password = :password WHERE id = :id";
    HashMap<String, Object> params = new HashMap<>();
    params.put("id", user.getId());
    params.put("name", user.getName());
    params.put("email", user.getEmail());
    params.put("password", user.getPassword());
    int rowsUpdated = namedParameterJdbcTemplate.update(sql, params);
    if (rowsUpdated != 1) {
      throw new RuntimeException("User not found with id: " + user.getId());
    }
  }
}
