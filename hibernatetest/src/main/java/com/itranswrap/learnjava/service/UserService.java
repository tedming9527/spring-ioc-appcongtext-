package com.itranswrap.learnjava.service;

import com.itranswrap.learnjava.entity.User;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class UserService {
  SessionFactory sessionFactory;
  public User register(String email, String password, String name) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    sessionFactory.getCurrentSession().persist(user);
    System.out.println(user.getId());
    return user;
  }
  public boolean deleteUser(Long id) {
    User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
    if (user != null) {
      sessionFactory.getCurrentSession().remove(user);
      return true;
    }
    return false;
  }
  public void updateUser(Long id, String name) {
    User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
    user.setName(name);
    sessionFactory.getCurrentSession().merge(user);
  }
  public User login(String email, String password) {
    List<User> list = sessionFactory.getCurrentSession().createNamedQuery("login", User.class)
        .setParameter("e", email)
        .setParameter("pwd", password)
        .list();
    return list.isEmpty() ? null : list.get(0);
  }
}
