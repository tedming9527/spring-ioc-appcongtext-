package com.itranswarp.learnjava.dao;

import com.itranswarp.learnjava.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserDaoImpl implements UserDao {
    
    private static final String TABLE_NAME = "users";
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User getById(long id) {
        return getUser("SELECT * FROM " + TABLE_NAME + " WHERE id = :id", 
                Map.of("id", id));
    }

    @Override
    public User getByName(String name) {
        return getUser("SELECT * FROM " + TABLE_NAME + " WHERE name = :name", 
                Map.of("name", name));
    }

    @Override
    public User getByEmail(String email) {
        return getUser("SELECT * FROM " + TABLE_NAME + " WHERE email = :email", 
                Map.of("email", email));
    }

    @Override
    public User getByNameAndPassword(String name, String password) {
        return getUser("SELECT * FROM " + TABLE_NAME + " WHERE name = :name AND password = :password",
                Map.of("name", name, "password", password));
    }

    private User getUser(String sql, Map<String, Object> params) {
        try {
            return jdbcTemplate.queryForObject(sql, params, USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public long insert(String name, String email, String password) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, email, password) VALUES (:name, :email, :password)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password);

        jdbcTemplate.update(sql, params, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = :name, email = :email, password = :password WHERE id = :id";
        Map<String, Object> params = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "password", user.getPassword()
        );
        updateAndCheckResult(sql, params, "更新用户失败，ID: " + user.getId());
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = :id";
        Map<String, Object> params = Map.of("id", user.getId());
        updateAndCheckResult(sql, params, "删除用户失败，ID: " + user.getId());
    }

    private void updateAndCheckResult(String sql, Map<String, Object> params, String errorMessage) {
        int affected = jdbcTemplate.update(sql, params);
        if (affected != 1) {
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public List<User> getAlls(int pageIndex, int pageSize) {
        String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT :limit OFFSET :offset";
        Map<String, Object> params = Map.of(
                "limit", pageSize,
                "offset", pageSize * pageIndex
        );
        return jdbcTemplate.query(sql, params, USER_ROW_MAPPER);
    }
}
