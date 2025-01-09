import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.itranswarp.learnjava.service")
public class Main {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

    UserService userService = context.getBean(UserService.class);
    User user = userService.login("bob@example.com", "password");
    System.out.println(user.getName());
    System.out.println(userService.getName());
  }
}
