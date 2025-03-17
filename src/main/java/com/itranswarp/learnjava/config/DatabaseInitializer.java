package com.itranswarp.learnjava.config;

import com.itranswarp.learnjava.entity.User;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.hibernate.Transaction;

@Component
public class DatabaseInitializer {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        try (var session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // 创建表
                session.createNativeQuery(DatabaseSQL.CREATE_USERS_TABLE, Void.class).executeUpdate();
                
                // 检查是否有数据
                Long count = session.createNativeQuery("SELECT COUNT(*) FROM users", Long.class)
                        .getSingleResult();

                // 只有在表为空时才初始化数据
                if (count == 0) {
                    session.createNativeQuery(DatabaseSQL.INIT_USERS_DATA, Void.class)
                            .executeUpdate();
                    System.out.println("数据库初始化完成");
                } else {
                    System.out.println("表已存在且有数据，跳过初始化");
                }
                
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException("数据库初始化失败: " + e.getMessage(), e);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("数据库初始化失败: " + e.getMessage(), e);
        }
    }
}
