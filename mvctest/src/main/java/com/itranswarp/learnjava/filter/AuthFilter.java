package com.itranswarp.learnjava.filter;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;
import com.itranswarp.learnjava.web.UserController;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class AuthFilter implements Filter {
  final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  UserService userService;
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String authHeader = request.getHeader("Authorization");
    logger.info("AuthFilter doFilter called. {}", authHeader);
    if (authHeader != null && authHeader.startsWith("Basic ")) {
      String email = prefixFrom(authHeader);
      String password = sufixFrom(authHeader);
      User user = userService.signin(email, password);

      request.getSession().setAttribute(UserController.KEY_USER, user);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }
  private String prefixFrom(String authHeader) {
    String base64Credentials = authHeader.substring("Basic ".length());
    String decoded = new String(Base64.getDecoder().decode(base64Credentials));
    int index = decoded.indexOf(':');
    if (index != -1) {
      return decoded.substring(0, index);
    }
    return null;
  }
  private String sufixFrom(String authHeader) {
    String base64Credentials = authHeader.substring("Basic ".length());
    String decoded = new String(Base64.getDecoder().decode(base64Credentials));
    int index = decoded.indexOf(':');
    if (index != -1) {
      return decoded.substring(index);
    }
    return null;
  }
}
