package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Validators {
  @Autowired
  List<Validator> validators;

  public void validate(String email, String password, String name) {
    for (var validator: this.validators) {
      validator.validate(email, password, name);
    }
  }
}
