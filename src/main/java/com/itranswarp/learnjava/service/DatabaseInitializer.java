package com.itranswarp.learnjava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class DatabaseInitializer {
    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
