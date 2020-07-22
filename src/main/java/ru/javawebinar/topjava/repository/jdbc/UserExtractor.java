package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            if (users.size() == 0 || id != users.get(users.size() - 1).getId()) {
                users.add(new User(id, rs.getString("name"), rs.getString("email"),
                        rs.getString("password"), rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"), rs.getDate("registered"),
                        EnumSet.of(Role.valueOf(rs.getString("role")))));
            } else {
                users.get(users.size() - 1).addRole(Role.valueOf(rs.getString("role")));
            }
        }
        return users;
    }
}
