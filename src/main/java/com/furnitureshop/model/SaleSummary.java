package com.furnitureshop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record SaleSummary(int id, Timestamp date, String customerName,
                          BigDecimal total, String paymentMethod) {
}
