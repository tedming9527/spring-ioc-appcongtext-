import com.itranswarp.learnjava.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.itranswarp.learnjava.service")
@EnableAspectJAutoProxy
public class Main {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    UserService userService = context.getBean(UserService.class);
    // userService.login("anna", "password");
    // userService.register("anna1", "anna1@example.com", "password");
  }
}
