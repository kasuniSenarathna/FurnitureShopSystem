package com.furnitureshop;

import com.furnitureshop.config.DBConnection;
import com.furnitureshop.util.Dialogs;
import com.furnitureshop.view.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // default look and feel is fine
        }

        // Check the database connection once at start-up
        try {
            DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            Dialogs.error(null, "Cannot connect to the database:\n" + e.getMessage()
                    + "\n\nCheck that MySQL is running and db.properties is correct.");
            return;
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
