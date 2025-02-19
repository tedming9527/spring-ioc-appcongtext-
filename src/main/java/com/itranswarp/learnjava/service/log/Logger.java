package com.itranswarp.learnjava.service.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {
  @Around("@annotation(Logging)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    // 获取方法名
    MethodSignature joinPoint1 = (MethodSignature) joinPoint.getSignature();
    String methodName = joinPoint1.toShortString();
    System.out.printf("方法 %s 开始执行\n", methodName);

    // 执行目标方法
    Object result;
    try {
      result = joinPoint.proceed();
    } catch (Throwable throwable) {
      System.out.printf("方法 %s 异常\n", methodName);
      throw throwable;
    }

    long elapsedTime = System.currentTimeMillis() - start;
    System.out.printf("方法 %s 执行完成，耗时 %d ms\n", methodName, elapsedTime);

    return result;
  }

}
