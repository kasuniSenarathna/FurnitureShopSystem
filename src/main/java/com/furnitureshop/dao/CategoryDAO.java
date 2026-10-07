package com.furnitureshop.dao;

import com.furnitureshop.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends BaseDAO {

    public List<Category> findAll() throws SQLException {
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM categories ORDER BY name")) {
            List<Category> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            return list;
        }
    }
}
