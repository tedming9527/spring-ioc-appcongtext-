package com.itranswarp.learnjava.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

public class MvcInterceptor implements HandlerInterceptor {
  @Autowired
  LocaleResolver localeResolver;

  @Autowired
  @Qualifier("i18n")
  MessageSource messageSource;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    if (modelAndView != null && modelAndView.getViewName() != null && !modelAndView.getViewName().startsWith("redirect:")) {
      Locale locale = localeResolver.resolveLocale(request);
      modelAndView.addObject("__messageSource__", messageSource);
      modelAndView.addObject("__locale__", locale);

    }
  }
}
