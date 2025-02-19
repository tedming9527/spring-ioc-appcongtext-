package com.itranswarp.learnjava.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据库配置类
 */
@Configuration
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class DatabaseConfig {
  // JDBC连接URL
  @Value("${jdbc.url}")
  private String jdbcUrl;
  // 数据库用户名
  @Value("${jdbc.username}")
  private String jdbcUsername;
  // 数据库密码
  @Value("${jdbc.password}")
  private String jdbcPassword;

  /**
   * 创建HikariCP数据源
   */
  @Bean
  public DataSource createDataSource() {
    JDBCDataSource dataSource = new JDBCDataSource();

    // 设置数据库连接信息
    dataSource.setURL(jdbcUrl);
    dataSource.setUser(jdbcUsername);
    dataSource.setPassword(jdbcPassword);

    return dataSource;
  }

  /**
   * 创建HikariCP数据源
   */
  @Bean
  public HikariDataSource createHikariDataSource() {
    HikariDataSource hikariDataSource = new HikariDataSource();

    // 设置数据库连接信息
    hikariDataSource.setJdbcUrl(jdbcUrl);
    hikariDataSource.setUsername(jdbcUsername);
    hikariDataSource.setPassword(jdbcPassword);

    // 设置连接池参数
    hikariDataSource.setAutoCommit(true);
    hikariDataSource.setIdleTimeout(60000);
    hikariDataSource.setMaxLifetime(1800000);
    return hikariDataSource;
  }

  /**
   * 创建命名参数JDBC模板
   */
  @Bean
  public NamedParameterJdbcTemplate createNamedParameterJdbcTemplate(@Autowired HikariDataSource hikariDataSource) {
    return new NamedParameterJdbcTemplate(hikariDataSource);
  }

  /**
   * 创建事务管理器
   */
  @Bean
  public DataSourceTransactionManager createDataSourceTransactionManager(@Autowired HikariDataSource hikariDataSource) {
    return new DataSourceTransactionManager(hikariDataSource);
  }

  @Bean
  LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
    var props = new Properties();
    props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产不要使用
    props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    props.setProperty("hibernate.show_sql", "true");

    var sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource);
    // 扫描指定的package获取所有的entity class;
    sessionFactoryBean.setPackagesToScan("com.itranswarp.learnjava.model");
    sessionFactoryBean.setHibernateProperties(props);
    return sessionFactoryBean;
  }

}