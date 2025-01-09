package com.itranswarp.learnjava.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {
  @Around("@annotation(Logging)")
  public Object logExecutionUserService(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long end = System.currentTimeMillis();
    System.out.println(joinPoint.getSignature() + " executed in " + (end - start) + "ms" + ", logging value is " + logging.value());
    return result;
  }
}
