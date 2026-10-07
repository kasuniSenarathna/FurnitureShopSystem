package com.furnitureshop.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO extends BaseDAO {

    public long countCustomers() throws SQLException {
        return count("SELECT COUNT(*) FROM customers");
    }

    public long countProducts() throws SQLException {
        return count("SELECT COUNT(*) FROM products");
    }

    public long countLowStock() throws SQLException {
        return count("SELECT COUNT(*) FROM products WHERE stock_qty <= reorder_level");
    }

    public BigDecimal salesToday() throws SQLException {
        return money("SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE DATE(sale_date) = CURDATE()");
    }

    public BigDecimal totalRevenue() throws SQLException {
        return money("SELECT COALESCE(SUM(total_amount), 0) FROM sales");
    }

    /** Sales totals for the last 7 days (days without sales are missing from the map). */
    public Map<LocalDate, BigDecimal> salesLast7Days() throws SQLException {
        String sql = "SELECT DATE(sale_date) AS d, SUM(total_amount) AS t FROM sales "
                + "WHERE sale_date >= CURDATE() - INTERVAL 6 DAY GROUP BY DATE(sale_date)";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            Map<LocalDate, BigDecimal> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getDate("d").toLocalDate(), rs.getBigDecimal("t"));
            }
            return map;
        }
    }

    private long count(String sql) throws SQLException {
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private BigDecimal money(String sql) throws SQLException {
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getBigDecimal(1);
        }
    }
}
