package com.itranswarp.learnjava.service;

import com.itranswarp.learnjava.entity.User;

import jakarta.transaction.Transactional;

import com.itranswarp.learnjava.aspect.Logging;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Transactional
public class UserService {

    private static final int DEFAULT_PAGE_SIZE = 100;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MailService mailService;

    @Logging(value = "login")
    @Transactional(rollbackOn = Exception.class)
    public void login(String email, String password) {
        try {
            var session = sessionFactory.getCurrentSession();
            List<User> users = session
                    .createQuery("FROM User WHERE email = :e AND password = :pwd", User.class)
                    .setParameter("e", email)
                    .setParameter("pwd", password)
                    .list();
            if (!users.isEmpty()) {
                mailService.sendLoginEmail(users.get(0));
            } else {
                throw new RuntimeException("用户名或密码错误");
            }
        } catch (Exception e) {
            throw new RuntimeException("用户登录失败: " + e.getMessage(), e);
        }
    }

  public void register(String name, String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    sessionFactory.getCurrentSession().persist(user);
    mailService.sendRegisterEmail(user);
    System.out.println(user.getId());
  }
  public boolean delete(Long id) {
    User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
    if (user != null) {
      sessionFactory.getCurrentSession().remove(user);
      return true;
    }
    return false;
  }
}
