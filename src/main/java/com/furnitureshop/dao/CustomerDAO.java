package com.furnitureshop.dao;

import com.furnitureshop.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends BaseDAO {

    public List<Customer> findAll(String keyword) throws SQLException {
        String sql = "SELECT id, name, phone, email, address FROM customers "
                + "WHERE name LIKE ? OR phone LIKE ? ORDER BY name";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Customer> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("phone"),
                            rs.getString("email"), rs.getString("address")));
                }
                return list;
            }
        }
    }

    public void insert(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, c.name());
            ps.setString(2, c.phone());
            ps.setString(3, c.email());
            ps.setString(4, c.address());
            ps.executeUpdate();
        }
    }

    public void update(Customer c) throws SQLException {
        String sql = "UPDATE customers SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, c.name());
            ps.setString(2, c.phone());
            ps.setString(3, c.email());
            ps.setString(4, c.address());
            ps.setInt(5, c.id());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM customers WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
