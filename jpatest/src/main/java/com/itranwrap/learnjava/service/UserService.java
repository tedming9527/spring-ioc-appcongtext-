package com.itranwrap.learnjava.service;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
  @Autowired
  private EntityManagerFactory em;

  public User getUserById(long id) {
    User user = this.em.find()
  }
}
