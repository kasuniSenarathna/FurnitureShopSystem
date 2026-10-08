package com.furnitureshop.controller;

import com.furnitureshop.dao.DashboardDAO;
import com.furnitureshop.dao.ProductDAO;
import com.furnitureshop.dao.SaleDAO;
import com.furnitureshop.model.DashboardData;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardController {

    private final DashboardDAO dashboardDAO = new DashboardDAO();
    private final SaleDAO saleDAO = new SaleDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public DashboardData load() throws SQLException {
        Map<LocalDate, BigDecimal> raw = dashboardDAO.salesLast7Days();
        Map<String, BigDecimal> chart = new LinkedHashMap<>();
        DateTimeFormatter label = DateTimeFormatter.ofPattern("dd MMM");
        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            chart.put(day.format(label), raw.getOrDefault(day, BigDecimal.ZERO));
        }
        return new DashboardData(
                dashboardDAO.countCustomers(),
                dashboardDAO.countProducts(),
                dashboardDAO.countLowStock(),
                dashboardDAO.salesToday(),
                dashboardDAO.totalRevenue(),
                chart,
                saleDAO.findRecent(8),
                productDAO.findLowStock());
    }
}
