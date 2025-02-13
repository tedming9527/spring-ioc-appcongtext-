package com.itranswarp.learnjava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @PostConstruct
  public void init() {
    String sql = "CREATE TABLE IF NOT EXISTS users ("
        + "id BIGINT NOT NULL PRIMARY KEY, "
        + "email VARCHAR(255) NOT NULL UNIQUE, "
        + "password VARCHAR(255) NOT NULL, "
        + "name VARCHAR(255) NOT NULL"
        + ");";

    // 执行 SQL 语句
    namedParameterJdbcTemplate.getJdbcTemplate().execute(sql);
    System.out.println("users 表创建完毕"+ e.getMessage());
  }
}
