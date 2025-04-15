package com.itranwrap.learnjava.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
  @Value("${jdbc.url}")
  String jdbcUrl;
  @Value("${jdbc.username}")
  String jdbcUsername;
  @Value("${jdbc.password}")
  String jdbcPassword;
  @Bean
  DataSource createDataSource() {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(jdbcUrl);
    config.setUsername(jdbcUsername);
    config.setPassword(jdbcPassword);

    return new HikariDataSource(config);
  }
  @Bean
  public LocalContainerEntityManagerFactoryBean createEntityManagerFactory(@Autowired DataSource dataSource) {
    var emFactory = new LocalContainerEntityManagerFactoryBean();
    emFactory.setDataSource(dataSource);
    emFactory.setPackagesToScan("com.itranswrap.learnjava.entity");
    emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    var props = new Properties();
    props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
    props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    props.setProperty("hibernate.show_sql", "true");
    emFactory.setJpaProperties(props);
    return emFactory;
  }
}
