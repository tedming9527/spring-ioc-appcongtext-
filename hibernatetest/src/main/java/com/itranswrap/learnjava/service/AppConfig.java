package com.itranswrap.learnjava.service;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
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

    config.addDataSourceProperty("autoCommit", "true");
    config.addDataSourceProperty("connectionTimeout", "5");
    config.addDataSourceProperty("idleTimeout", "60");

    return new HikariDataSource(config);
  }
  @Bean
  HibernateTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
    return new HibernateTransactionManager(sessionFactory);
  }
  @Bean
  LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
    var props = new Properties();
    props.setProperty("hibernate.hbm2ddl.auto", "update");
    props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    props.setProperty("hibernate.show_sql", "true");

    var sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource);
    sessionFactoryBean.setPackagesToScan("com.itranswrap.learnjava.entity");
    sessionFactoryBean.setHibernateProperties(props);
    return sessionFactoryBean;
  }
}
