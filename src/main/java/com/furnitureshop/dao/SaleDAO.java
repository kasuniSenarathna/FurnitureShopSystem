package com.furnitureshop.dao;

import com.furnitureshop.exception.InsufficientStockException;
import com.furnitureshop.model.CartItem;
import com.furnitureshop.model.SaleSummary;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO extends BaseDAO {

    /**
     * Saves the bill header, the bill lines and reduces the stock in ONE transaction.
     * If anything fails (for example not enough stock) everything is rolled back.
     */
    public int save(int customerId, int userId, BigDecimal discount, BigDecimal total,
                    String paymentMethod, List<CartItem> items)
            throws SQLException, InsufficientStockException {

        Connection con = conn();
        boolean oldAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            int saleId;
            String headerSql = "INSERT INTO sales (customer_id, user_id, discount, total_amount, payment_method) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(headerSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customerId);
                ps.setInt(2, userId);
                ps.setBigDecimal(3, discount);
                ps.setBigDecimal(4, total);
                ps.setString(5, paymentMethod);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    saleId = keys.getInt(1);
                }
            }

            String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, line_total) "
                    + "VALUES (?, ?, ?, ?, ?)";
            String stockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
            try (PreparedStatement itemPs = con.prepareStatement(itemSql);
                 PreparedStatement stockPs = con.prepareStatement(stockSql)) {
                for (CartItem item : items) {
                    stockPs.setInt(1, item.quantity());
                    stockPs.setInt(2, item.product().id());
                    stockPs.setInt(3, item.quantity());
                    if (stockPs.executeUpdate() == 0) {
                        throw new InsufficientStockException(item.product().name());
                    }

                    itemPs.setInt(1, saleId);
                    itemPs.setInt(2, item.product().id());
                    itemPs.setInt(3, item.quantity());
                    itemPs.setBigDecimal(4, item.product().price());
                    itemPs.setBigDecimal(5, item.lineTotal());
                    itemPs.executeUpdate();
                }
            }

            con.commit();
            return saleId;

        } catch (SQLException | InsufficientStockException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    public List<SaleSummary> findRecent(int limit) throws SQLException {
        String sql = "SELECT s.id, s.sale_date, c.name AS customer_name, s.total_amount, s.payment_method "
                + "FROM sales s JOIN customers c ON c.id = s.customer_id "
                + "ORDER BY s.sale_date DESC, s.id DESC LIMIT ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<SaleSummary> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new SaleSummary(rs.getInt("id"), rs.getTimestamp("sale_date"),
                            rs.getString("customer_name"), rs.getBigDecimal("total_amount"),
                            rs.getString("payment_method")));
                }
                return list;
            }
        }
    }
}
