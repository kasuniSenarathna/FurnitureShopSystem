package com.furnitureshop.controller;

import com.furnitureshop.config.DBConnection;
import com.furnitureshop.util.SessionManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportController {

    /** Compiles the JRXML template and fills it with data from MySQL. */
    public JasperPrint buildSalesReport() throws JRException, SQLException, IOException {
        try (InputStream in = ReportController.class.getResourceAsStream("/reports/sales_report.jrxml")) {
            if (in == null) {
                throw new IOException("Report template /reports/sales_report.jrxml was not found.");
            }
            JasperReport report = JasperCompileManager.compileReport(in);

            Map<String, Object> params = new HashMap<>();
            params.put("SHOP_NAME", "Furniture Shop");
            params.put("PRINTED_BY", SessionManager.getInstance().getCurrentUser().fullName());

            return JasperFillManager.fillReport(report, params, DBConnection.getInstance().getConnection());
        }
    }
}
