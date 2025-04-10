package com.itranswarp.learnjava.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  long id;
  String email;
  String password;
  String name;
}
