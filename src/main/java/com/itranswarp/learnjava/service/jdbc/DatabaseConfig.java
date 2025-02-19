package com.itranswarp.learnjava.service.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据库配置类
 */
@Configuration
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

}