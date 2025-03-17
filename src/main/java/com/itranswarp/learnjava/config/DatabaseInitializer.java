package com.itranswarp.learnjava.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        try {
            // 检查表中是否已有数据
            Query query = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM users");
            long count = query.getResultCount();

            // 只有在表为空时才初始化数据
            if (count == 0) {
                Query query = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM users");
                namedParameterJdbcTemplate.getJdbcTemplate().execute(DatabaseSQL.INIT_USERS_DATA);
                System.out.println("数据库初始化完成");
            } else {
                System.out.println("表已存在且有数据，跳过初始化");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("数据库初始化失败", e);
        }
    }

}
