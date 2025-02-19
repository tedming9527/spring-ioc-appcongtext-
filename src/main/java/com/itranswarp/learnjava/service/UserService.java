package com.itranswarp.learnjava.service;

import com.itranswarp.learnjava.dao.UserDao;
import com.itranswarp.learnjava.model.User;
import com.itranswarp.learnjava.aspect.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {
    
    private static final int DEFAULT_PAGE_SIZE = 100;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private MailService mailService;

    @Logging(value = "login")
    public void login(String name, String password) {
        User user = userDao.getByNameAndPassword(name, password);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        mailService.sendLoginEmail(user);
    }

    public void register(String name, String email, String password) {
        try {
            long userId = userDao.insert(name, email, password);
            User user = userDao.getById(userId);
            mailService.sendRegisterEmail(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("用户名或邮箱已存在", e);
        }
    }

    public User getUserById(long id) {
        User user = userDao.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }
        return user;
    }

    public User getUserByName(String name) {
        User user = userDao.getByName(name);
        if (user == null) {
            throw new RuntimeException("用户不存在，用户名: " + name);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userDao.getByEmail(email);
        if (user == null) {
            throw new RuntimeException("用户不存在，邮箱: " + email);
        }
        return user;
    }

    public List<User> getUsers(int pageIndex) {
        return userDao.getAlls(pageIndex, DEFAULT_PAGE_SIZE);
    }

    public void updateUser(User user) {
        userDao.update(user);
    }
}
