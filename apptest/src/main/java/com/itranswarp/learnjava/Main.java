package com.itranswarp.learnjava;

import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.ZoneId;

@Configuration
@ComponentScan("com.itranswarp.learnjava")
@EnableAspectJAutoProxy
public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    UserService userService = context.getBean(UserService.class);
    User user = userService.login("bob@example.com", "password");
    ZoneId zoneId = userService.zoneId;
    System.err.println(zoneId);
    userService.sayHi();
  }
}
