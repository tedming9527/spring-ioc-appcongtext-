package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;

@Component
public class UserService {
  @Autowired
  private MailService mailService;
  private List<User> users = new ArrayList<>();
  public UserService() {
    User user1 = new User(1, "anna", "anna@example.com", "password");
    User user2 = new User(2, "alice", "alice@example.com", "password");
    User user3 = new User(3, "bob", "bob@example.com", "password");
    users.add(user1);
    users.add(user2);
    users.add(user3);
  }

  @Logging(value = "login")
  public void login(String name, String password) {
    User currentUser = null;
    for(User user: users) {
      if (Objects.equals(user.getName(), name) && Objects.equals(user.getPassword(), password)) {
        currentUser = user;
        break;
      }
    }
    if (currentUser == null) {
      throw new Error("账号不存在或密码错误");
    }
    mailService.sendLoginEmail(currentUser);
  }
  public void register(String name,String email, String password) {
    User currentUser = null;
    for(User user: users) {
      if (Objects.equals(user.getName(), name)) {
        currentUser = user;
        break;
      }
    }
    if (currentUser != null) {
      throw new Error(String.format("账号%s已经存在", name));
    }
    OptionalLong id = users.stream().mapToLong(User::getId).max();
    User user = new User(id.orElse(0) + 1, name, email, password);
    users.add(user);
    mailService.sendRegisterEmail(user);
  }
}
