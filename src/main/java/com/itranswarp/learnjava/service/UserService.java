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
public class UserService {

    private static final int DEFAULT_PAGE_SIZE = 100;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MailService mailService;

    @Logging(value = "login")
    @Transactional
    public void login(String email, String password) {
        try {
            var session = sessionFactory.getCurrentSession();
            List<User> users = session
                .createNativeQuery("SELECT * FROM users WHERE email = :e AND password = :pwd", User.class)
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

    @Transactional
    public void register(String name, String email, String password) {
        try {
            // 检查邮箱是否已存在
            var session = sessionFactory.getCurrentSession();
            List<User> existingUsers = session
                .createNativeQuery("SELECT * FROM users WHERE email = :e", User.class)
                .setParameter("e", email)
                .list();
            if (!existingUsers.isEmpty()) {
                throw new RuntimeException("邮箱已被注册");
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            session.persist(user);
            mailService.sendRegisterEmail(user);
        } catch (Exception e) {
            throw new RuntimeException("用户注册失败: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
            if (user != null) {
                sessionFactory.getCurrentSession().remove(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("删除用户失败: " + e.getMessage(), e);
        }
    }
}
