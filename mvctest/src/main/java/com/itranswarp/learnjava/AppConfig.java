package com.itranswarp.learnjava;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.Servlet5Loader;
import io.pebbletemplates.spring.servlet.PebbleViewResolver;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.sql.DataSource;
import java.io.File;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("classpath:jdbc.properties")
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

    // 设置 JDBC 驱动类
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(jdbcUsername);
    config.setPassword(jdbcPassword);
    config.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");  // 设置 HSQLDB 的 JDBC 驱动类
    config.addDataSourceProperty("autoCommit", "false");
    config.addDataSourceProperty("connectionTimeout", "5");
    config.addDataSourceProperty("idleTimeout", "60");

    return new HikariDataSource(config);
  }

  @Bean
  JdbcTemplate createJdbcTemplete(@Autowired DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
    var engine = new PebbleEngine.Builder().autoEscaping(true)
        .cacheActive(false)
        .loader(new Servlet5Loader(servletContext))
        .build();
    var viewResolver = new PebbleViewResolver(engine);
    viewResolver.setPrefix("/WEB-INF/templates/");
    viewResolver.setSuffix("");
    return viewResolver;
  }

  @Bean
  WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
    return new WebMvcConfigurer() {
      @Override
      public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
      }

      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        for (var interceptor: interceptors) {
          registry.addInterceptor(interceptor).addPathPatterns("/api/**");
        }
      }

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
          .allowedOrigins("http://local.liaoxuefeng.com:8080")
          .allowedMethods("GET", "POST")
          .maxAge(3600);
      }
    };
  }

  public static void main(String[] args) throws Exception {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(Integer.getInteger("port", 8080));
    tomcat.getConnector();
    Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
    WebResourceRoot resources = new StandardRoot(ctx);
    resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
    ctx.setResources(resources);
    tomcat.start();
    tomcat.getServer().await();

  }
  @Primary
  @Bean
  LocaleResolver createLocaleResolver() {
    var clr = new CookieLocaleResolver();
    clr.setDefaultLocale(Locale.ENGLISH);
    clr.setDefaultTimeZone(TimeZone.getDefault());
    return clr;
  }

  @Bean("i18n")
  MessageSource createMessageSource() {
    var messageSource = new ResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasename("messages");
    return messageSource;
  }

}