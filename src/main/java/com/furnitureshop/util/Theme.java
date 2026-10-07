package com.furnitureshop.util;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;

public final class Theme {

    public static final Color DARK = new Color(0x4E342E);
    public static final Color PRIMARY = new Color(0x6D4C41);
    public static final Color ACCENT = new Color(0xA1887F);
    public static final Color BG = new Color(0xF5F0EB);
    public static final Color CARD = Color.WHITE;
    public static final Color DANGER = new Color(0xC62828);
    public static final Color SUCCESS = new Color(0x2E7D32);

    public static final Font FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 14);
    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 22);

    private Theme() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE);
        label.setForeground(DARK);
        return label;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(FONT);
        table.setGridColor(new Color(0xE0D6D0));
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }

    /** Adds "label : field" as one row of a GridBagLayout form. */
    public static void addRow(JPanel panel, int row, String labelText, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(6, 6, 6, 6);
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BOLD);
        panel.add(label, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        field.setFont(FONT);
        panel.add(field, c);
    }

    public static String money(BigDecimal value) {
        return "Rs. " + String.format("%,.2f", value);
    }
}
