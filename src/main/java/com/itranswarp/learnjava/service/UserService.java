package com.itranswarp.learnjava.service;

import com.itranswarp.learnjava.entity.User;
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
    public User login(String email, String password) {
        List<User> users = sessionFactory.getCurrentSession().createNamedQuery("login", User.class).setParameter("e", email).setParameter("pwd",password).list();
        return users.isEmpty() ? null : users.get(0);
    }

    public void register(String name, String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        sessionFactory.getCurrentSession().persist(user);
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
