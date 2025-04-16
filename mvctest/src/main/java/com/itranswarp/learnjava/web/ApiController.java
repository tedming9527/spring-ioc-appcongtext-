package com.itranswarp.learnjava.web;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://local.liaoxuefeng.com:8080")
@RestController
@RequestMapping("/api")
public class ApiController {
  @Autowired
  UserService userService;

  @GetMapping("/users")
  public List<User> users() {
    return userService.getUsers();
  }
  @GetMapping("users/{id}")
  public User user(@PathVariable("id") long id) {
    return userService.getUserById(id);
  }
  @PostMapping("signin")
  public Map<String, Object> sigin(@RequestBody SignInRequest signInRequest) {
    try {
      User user = userService.signin(signInRequest.email, signInRequest.password);
      System.out.println("signin password: " + user.getPassword());
      return Map.of("user", user);
    } catch (Exception e) {
      return Map.of("error", "SIGNIN_FAILED", "message", e.getMessage());
    }

  }
  public static class SignInRequest {
    public String email;
    public String password;
  }
}
