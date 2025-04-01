package com.itranswarp.learnjava.service;
import com.itranswarp.learnjava.model.User;
import org.springframework.stereotype.Component;

@Component
public class MailService {
  public void sendLoginEmail(User user) {
    System.out.printf("mail to %s's email: %s, logined\n", user.getName(), user.getEmail());
  }
  public void sendRegisterEmail (User user) {
    System.out.printf("mail to %s's email: %s, register\n", user.getName(), user.getEmail());
  }
}
