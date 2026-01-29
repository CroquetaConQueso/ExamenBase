package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UiTheme {

    private UiTheme() {}

    // Paleta
    public static final Color PANEL  = new Color(0x0B1220);
    public static final Color CARD   = new Color(0x0F172A);
    public static final Color BORDER = new Color(0x263244);

    public static final Color TEXT   = new Color(0xF3F4F6); // para labels en fondo oscuro
    public static final Color MUTED  = new Color(0xC7CDD6);
    public static final Color DISABLED_TEXT = new Color(0x6B7280); // gris más visible

    public static final Color PRIMARY = new Color(0x3B82F6);
    public static final Color DANGER  = new Color(0xEF4444);
    public static final Color NEUTRAL = new Color(0x1F2937);

    // >>> CLAVE: texto de botones negro (para que nunca sea “hueso” sobre blanco)
    public static final Color BUTTON_TEXT = Color.BLACK;
    public static final Color BUTTON_BG   = Color.WHITE;

    public static final Font FONT_BASE  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);

    public static Border roundedBorder() {
        return roundedBorder(BORDER);
    }

    public static Border roundedBorder(Color line) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Paneles / labels
        put("Panel.background", PANEL);
        put("Viewport.background", PANEL);
        put("Label.foreground", TEXT);
        put("Label.font", FONT_BASE);
        put("Label.disabledForeground", DISABLED_TEXT);

        // Inputs
        put("TextField.font", FONT_BASE);
        put("TextField.background", new ColorUIResource(CARD));
        put("TextField.foreground", new ColorUIResource(TEXT));
        put("TextField.caretForeground", TEXT);
        put("TextField.inactiveForeground", DISABLED_TEXT);
        put("TextField.inactiveBackground", CARD);
        put("TextField.disabledBackground", CARD);
        put("TextField.border", roundedBorder());

        put("FormattedTextField.font", FONT_BASE);
        put("FormattedTextField.background", new ColorUIResource(CARD));
        put("FormattedTextField.foreground", new ColorUIResource(TEXT));
        put("FormattedTextField.caretForeground", TEXT);
        put("FormattedTextField.inactiveForeground", DISABLED_TEXT);
        put("FormattedTextField.inactiveBackground", CARD);
        put("FormattedTextField.disabledBackground", CARD);
        put("FormattedTextField.border", roundedBorder());

        // ComboBox
        put("ComboBox.font", FONT_BASE);
        put("ComboBox.background", new ColorUIResource(CARD));
        put("ComboBox.foreground", new ColorUIResource(TEXT));
        put("ComboBox.selectionBackground", PRIMARY);
        put("ComboBox.selectionForeground", Color.WHITE);
        put("ComboBox.disabledBackground", CARD);
        put("ComboBox.disabledForeground", DISABLED_TEXT);
        put("ComboBox.border", roundedBorder());

        // Lista del ComboBox
        put("List.background", CARD);
        put("List.foreground", TEXT);
        put("List.selectionBackground", PRIMARY);
        put("List.selectionForeground", Color.WHITE);

        // Checkbox
        put("CheckBox.font", FONT_BASE);
        put("CheckBox.foreground", TEXT);
        put("CheckBox.background", PANEL);
        put("CheckBox.disabledText", DISABLED_TEXT);

        // Scrollpane
        put("ScrollPane.background", PANEL);
        put("ScrollPane.border", BorderFactory.createLineBorder(BORDER, 1, true));

        // OptionPane
        put("OptionPane.background", PANEL);
        put("OptionPane.messageForeground", TEXT);

        // Tabla
        put("Table.font", FONT_BASE);
        put("Table.foreground", new ColorUIResource(TEXT));
        put("Table.background", new ColorUIResource(CARD));
        put("Table.selectionBackground", new Color(0x1D4ED8));
        put("Table.selectionForeground", Color.WHITE);
        put("Table.gridColor", BORDER);
        put("Table.focusCellBackground", new Color(0x1D4ED8));
        put("Table.focusCellForeground", Color.WHITE);

        // Header tabla
        put("TableHeader.font", FONT_BOLD);
        put("TableHeader.background", new ColorUIResource(NEUTRAL));
        put("TableHeader.foreground", new ColorUIResource(TEXT));
        put("TableHeader.cellBorder", BorderFactory.createLineBorder(BORDER, 1));
        put("TableHeader.opaque", Boolean.TRUE);

        // >>> CLAVE: Botones por defecto (nuevo JButton("..."))
        put("Button.font", FONT_BOLD);
        put("Button.background", new ColorUIResource(BUTTON_BG));
        put("Button.foreground", new ColorUIResource(BUTTON_TEXT));
        put("Button.disabledText", DISABLED_TEXT);
        put("Button.disabledForeground", DISABLED_TEXT);
        put("Button.border", roundedBorder());
        put("Button.focus", new Color(0, 0, 0, 0));
    }

    private static void put(String key, Object value) {
        UIManager.put(key, value);
    }

    private static JButton baseButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));

        // Fuerza legibilidad
        b.setBackground(BUTTON_BG);
        b.setForeground(BUTTON_TEXT);

        return b;
    }

    public static JButton primary(String text) {
        JButton b = baseButton(text);
        // Fondo blanco + borde azul (texto negro)
        b.setBorder(roundedBorder(PRIMARY));
        return b;
    }

    public static JButton danger(String text) {
        JButton b = baseButton(text);
        // Fondo blanco + borde rojo (texto negro)
        b.setBorder(roundedBorder(DANGER));
        return b;
    }

    public static JButton neutral(String text) {
        JButton b = baseButton(text);
        b.setBorder(roundedBorder(BORDER));
        return b;
    }

    public static void styleTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT);
    }

    public static void forceTableHeaderStyle(JTable table) {
        if (table == null) return;
        JTableHeader h = table.getTableHeader();
        if (h == null) return;

        h.setOpaque(true);
        h.setBackground(NEUTRAL);
        h.setForeground(TEXT);
        h.setFont(FONT_BOLD);

        h.setDefaultRenderer((tbl, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);
            label.setBackground(NEUTRAL);
            label.setForeground(TEXT);
            label.setFont(FONT_BOLD);
            label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, BORDER),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        });
    }

    public static void styleTextField(JTextField field) {
        if (field == null) return;
        field.setBackground(CARD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(FONT_BASE);
        field.setBorder(roundedBorder());
    }

    public static void styleComboBox(JComboBox<?> combo) {
        if (combo == null) return;
        combo.setBackground(CARD);
        combo.setForeground(TEXT);
        combo.setFont(FONT_BASE);
        combo.setBorder(roundedBorder());
    }
}


