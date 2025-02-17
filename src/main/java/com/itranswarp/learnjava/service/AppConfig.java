package com.itranswarp.learnjava.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Component
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
    // HikariCP 配置
    HikariConfig hikariConfig = new HikariConfig();

    // 设置数据库驱动和 URL（内存模式）
    hikariConfig.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
    hikariConfig.setJdbcUrl(jdbcUrl);  // 内存模式的数据库 URL
    hikariConfig.setUsername(jdbcUsername);                      // 用户名
    hikariConfig.setPassword(jdbcPassword);
    hikariConfig.setAutoCommit(true);
    hikariConfig.setConnectionTimeout(5000);
    hikariConfig.setIdleTimeout(6000);
    return  new HikariDataSource(hikariConfig);
  }

  @Bean
  NamedParameterJdbcTemplate createNamedParameterJdbcTemplate(@Autowired DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }
  @Bean
  PlatformTransactionManager createTransactionManager(@Autowired DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
