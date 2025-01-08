import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan
public class Main {
  public static void main(String[] args) {
    // 创建一个容器上下文，并驾照Main这个@Configuration类来进行扫描,@ComponentScan决定自动扫描范围
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    context.register(Main.class);
    context.refresh();

    UserService userService = context.getBean(UserService.class);
    User user = userService.login("bob@example.com", "password");
    System.out.println(user.getName());
  }
}
