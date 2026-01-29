package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UiTheme {
    public static final Color BG_DARK = new Color(15, 15, 15);
    public static final Color SURFACE = new Color(30, 30, 30);
    public static final Color TEXT_MAIN = new Color(240, 240, 240);
    public static final Color ACCENT = new Color(0, 255, 204);
    public static final Color DANGER = new Color(255, 60, 60);
    public static final Color NEUTRAL = new Color(255, 255, 0);

    public static void install() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("Label.foreground", TEXT_MAIN);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT_MAIN);
        UIManager.put("Table.selectionBackground", ACCENT);
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("OptionPane.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
    }

    public static void styleTextField(JTextField f) {
        f.setBackground(new Color(45, 45, 45));
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT), BorderFactory.createEmptyBorder(5,5,5,5)));
    }

    public static void forceTableHeaderStyle(JTable t) {
        JTableHeader h = t.getTableHeader();
        h.setBackground(Color.BLACK);
        h.setForeground(ACCENT);
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    public static JButton createBtn(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return b;
    }
}