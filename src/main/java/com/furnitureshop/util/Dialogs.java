package com.furnitureshop.util;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.sql.SQLException;

public final class Dialogs {

    private Dialogs() {
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warn(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Please check", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Turns common MySQL error codes into friendly messages. */
    public static void sqlError(Component parent, SQLException e) {
        String message;
        if (e.getErrorCode() == 1062) {
            message = "This record already exists (duplicate value, for example the phone number).";
        } else if (e.getErrorCode() == 1451) {
            message = "This record is used by other records (for example sales) and cannot be deleted.";
        } else {
            message = "Database error: " + e.getMessage();
        }
        error(parent, message);
    }
}
