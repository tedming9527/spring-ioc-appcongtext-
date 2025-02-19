package com.itranswarp.learnjava;// 移除包声明，因为该类位于默认包中

import com.itranswarp.learnjava.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.itranswarp.learnjava")
@EnableAspectJAutoProxy
public class Main {
    
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = createApplicationContext();
             Scanner scanner = new Scanner(System.in)) {
            
            startApplication(context);
            waitForExit(scanner);
            
        } catch (Exception e) {
            System.err.println("应用程序启动失败: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static AnnotationConfigApplicationContext createApplicationContext() {
        return new AnnotationConfigApplicationContext(Main.class);
    }
    
    private static void startApplication(AnnotationConfigApplicationContext context) {
        try {
            UserService userService = context.getBean(UserService.class);
            userService.login("anna", "password");
            System.out.println("应用程序已启动，按回车键退出...");
        } catch (Exception e) {
            throw new RuntimeException("用户登录失败: " + e.getMessage(), e);
        }
    }
    
    private static void waitForExit(Scanner scanner) {
        scanner.nextLine();
        System.out.println("应用程序已退出");
    }
}
