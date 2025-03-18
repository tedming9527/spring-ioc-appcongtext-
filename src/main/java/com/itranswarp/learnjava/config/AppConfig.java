package com.itranswarp.learnjava.config;


import java.util.Properties;

import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
	// JDBC连接URL
  @Value("${jdbc.url}")
  private String jdbcUrl;
  // 数据库用户名
  @Value("${jdbc.username}")
  private String jdbcUsername;
  // 数据库密码
  @Value("${jdbc.password}")
  private String jdbcPassword;
	@Bean
	public DataSource createDataSource() {
		JDBCDataSource dataSource = new JDBCDataSource();

		// 设置数据库连接信息
		dataSource.setURL(jdbcUrl);
		dataSource.setUser(jdbcUsername);
		dataSource.setPassword(jdbcPassword);

		return dataSource;
	}
	@Bean
	LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
		var props = new Properties();
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.format_sql", "true");
		props.setProperty("hibernate.current_session_context_class","thread");
		// 修改事务配置
		// props.setProperty("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
		
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setPackagesToScan("com.itranswarp.learnjava.entity");
		sessionFactoryBean.setHibernateProperties(props);
		return sessionFactoryBean;
	}

	@Bean
	HibernateTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager= new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}
}
