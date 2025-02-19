import com.itranswarp.learnjava.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.itranswarp.learnjava.service")
@EnableAspectJAutoProxy
public class Main {
    public static void main(String[] args) {
        System.out.println("应用程序已启动,main执行...");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("应用程序已启动, 开始注册事件...");
            // 等待数据库就绪事件
            context.addApplicationListener((org.springframework.context.ApplicationEvent event) -> {
                if (event.getClass().getSimpleName().equals("DatabaseReadyEvent")) {
                    UserService userService = context.getBean(UserService.class);
                    userService.login("anna", "password");
                    System.out.println("应用程序已启动，按回车键退出...");
                }
            });

            scanner.nextLine();
        }
    }
}
