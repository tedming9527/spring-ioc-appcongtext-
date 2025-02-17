package com.itranswarp.learnjava.service;

import jakarta.annotation.PostConstruct;
import org.hsqldb.server.ServerAcl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;

import java.io.IOException;

@Component
public class DatabaseInitializer {
// 删除重复的@Autowired注解
    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Server hsqlServer;

    // 在构造函数中启动数据库服务器
    public DatabaseInitializer() {
        try {
            startDB();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start database server", e);
        }
    }

    private void startDB() throws ServerAcl.AclFormatException, IOException {
        hsqlServer = new Server();
        HsqlProperties properties = new HsqlProperties();
        properties.setProperty("server.database.0", "mem:testdb");
        properties.setProperty("server.dbname.0", "testdb");
        hsqlServer.setProperties(properties);
        hsqlServer.start();
        System.out.println("HSQLDB 服务器已启动");
    }

    @PostConstruct
    public void init() {
        try {
            // 创建表
            namedParameterJdbcTemplate.getJdbcTemplate().execute(DatabaseSQL.CREATE_USERS_TABLE);
            
            // 初始化数据
            namedParameterJdbcTemplate.getJdbcTemplate().execute(DatabaseSQL.INIT_USERS_DATA);
            
            // 发布数据库就绪事件
            eventPublisher.publishEvent(new DatabaseReadyEvent(this));
        } catch (DataAccessException e) {
            throw new RuntimeException("数据库初始化失败", e);
        }
    }
}
