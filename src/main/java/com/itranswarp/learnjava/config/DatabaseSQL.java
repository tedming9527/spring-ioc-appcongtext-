package com.itranswarp.learnjava.config;

public class DatabaseSQL {

  public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users ("
      + "id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,"
      + "email VARCHAR(255) NOT NULL UNIQUE,"
      + "password VARCHAR(255) NOT NULL,"
      + "name VARCHAR(255) NOT NULL"
      + ")";

  public static final String INIT_USERS_DATA = "INSERT INTO users (email, password, name) VALUES "
      + "('anna@example.com', 'password', 'anna'), "
      + "('bob@example.com', 'password', 'bob'), "
      + "('alice@example.com', 'password', 'alice')";

}
