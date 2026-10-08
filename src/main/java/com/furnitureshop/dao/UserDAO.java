package com.furnitureshop.dao;

import com.furnitureshop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO {

    /** Returns the user if username and password hash match, otherwise null. */
    public User authenticate(String username, String passwordHash) throws SQLException {
        String sql = "SELECT id, username, full_name, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"),
                            rs.getString("full_name"), rs.getString("role"));
                }
                return null;
            }
        }
    }
}
