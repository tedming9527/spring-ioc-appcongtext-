import com.itranswarp.learnjava.service.DatabaseInitializer;
import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.hsqldb.server.ServerAcl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "com.itranswarp.learnjava.service")
@EnableAspectJAutoProxy
public class Main {
  public static void main(String[] args) {
    try {
      DatabaseInitializer.startDB();
    } catch (IOException | ServerAcl.AclFormatException e) {
      throw new RuntimeException("Start DB, " + e.getMessage());
    }


    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    UserService userService = context.getBean(UserService.class);

    userService.register("anna", "anna@example.com", "password");
    userService.register("bob", "bob@example.com", "password");
    userService.register("alice", "alice@example.com", "password");
    userService.login("anna", "password");

    Scanner scanner = new Scanner(System.in);
    scanner.nextInt();
  }
}
