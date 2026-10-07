package com.furnitureshop.dao;

import com.furnitureshop.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends BaseDAO {

    private static final String SELECT =
            "SELECT p.id, p.category_id, c.name AS category_name, p.name, p.material, "
                    + "p.price, p.stock_qty, p.reorder_level "
                    + "FROM products p JOIN categories c ON c.id = p.category_id ";

    public List<Product> findAll(String keyword) throws SQLException {
        String sql = SELECT + "WHERE p.name LIKE ? OR c.name LIKE ? OR p.material LIKE ? ORDER BY p.name";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                return readAll(rs);
            }
        }
    }

    public List<Product> findLowStock() throws SQLException {
        String sql = SELECT + "WHERE p.stock_qty <= p.reorder_level ORDER BY p.stock_qty";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return readAll(rs);
        }
    }

    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, material, price, stock_qty, reorder_level) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, p.categoryId());
            ps.setString(2, p.name());
            ps.setString(3, p.material());
            ps.setBigDecimal(4, p.price());
            ps.setInt(5, p.stockQty());
            ps.setInt(6, p.reorderLevel());
            ps.executeUpdate();
        }
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, name = ?, material = ?, price = ?, "
                + "stock_qty = ?, reorder_level = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, p.categoryId());
            ps.setString(2, p.name());
            ps.setString(3, p.material());
            ps.setBigDecimal(4, p.price());
            ps.setInt(5, p.stockQty());
            ps.setInt(6, p.reorderLevel());
            ps.setInt(7, p.id());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Product> readAll(ResultSet rs) throws SQLException {
        List<Product> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Product(rs.getInt("id"), rs.getInt("category_id"), rs.getString("category_name"),
                    rs.getString("name"), rs.getString("material"), rs.getBigDecimal("price"),
                    rs.getInt("stock_qty"), rs.getInt("reorder_level")));
        }
        return list;
    }
}
