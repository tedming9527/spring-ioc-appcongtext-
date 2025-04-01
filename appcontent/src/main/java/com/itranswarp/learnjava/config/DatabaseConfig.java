package com.itranswarp.learnjava.config;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 数据库配置类
 */
@Configuration
@MapperScan("com.itranswarp.learnjava.mapper")
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

  @Bean
  SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
    var sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    return  sqlSessionFactoryBean;
  }
  @Bean
  PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
    return  new DataSourceTransactionManager(dataSource);
  }

}