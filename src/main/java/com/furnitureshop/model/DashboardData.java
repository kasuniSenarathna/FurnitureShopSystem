package com.furnitureshop.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardData(long customers, long products, long lowStock,
                            BigDecimal salesToday, BigDecimal revenue,
                            Map<String, BigDecimal> chart,
                            List<SaleSummary> recentSales,
                            List<Product> lowStockItems) {
}
