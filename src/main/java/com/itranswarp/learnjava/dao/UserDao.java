package com.itranswarp.learnjava.service.dao;

import com.itranswarp.learnjava.service.User;

import java.util.List;

public interface UserDao {
  User getById(long id);
  User getByName(String name);
  User getByEmail(String email);
  User getByNameAndPassword(String name, String password);
  long insert(String name, String email, String password);
  void update(User user);
  void delete(User user);
  List<User> getAlls(int pageIndex, int pageSize);
}
