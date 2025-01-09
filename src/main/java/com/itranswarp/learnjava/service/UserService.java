package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:application-prod.properties")
public class UserService {
  @Autowired
  private MailService mailService;

  @Value("${name}")
  private String name;

  public void setMailService(MailService mailService) {
    this.mailService = mailService;
  }
  private List<User> users = new ArrayList<>(List.of(
      new User(1, "bob@example.com", "password", "Bob"),
      new User(2, "alice@example.com", "password", "Alice"),
      new User(3, "tom@example.com", "password", "Tom")
  ));

  @Logging("login")
  public User login(String email, String password) {
    for (User user: users) {
      if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
        mailService.sendLoginMail(user);
        return user;
      }
    }
    throw new RuntimeException("login failed.");
  }
  public User getUser(long id) {
    return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
  }
  public User register(String email, String password, String name) {
    users.forEach(user -> {
      if (user.getEmail().equalsIgnoreCase(email)) {
        throw new RuntimeException("email exist.");
      }
    });
    User user = new User(users.stream().mapToLong(User::getId).max().getAsLong() + 1, email, password, name);
    users.add(user);
    mailService.sendRegistrationMail(user);
    return user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
