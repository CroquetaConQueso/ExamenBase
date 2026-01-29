package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UiTheme {
    // Paleta basada en la imagen de referencia (Estilo "Empresa")
    public static final Color BLUE_HEADER = new Color(130, 160, 190); // Azul grisáceo de las cabeceras
    public static final Color BG_LIGHT    = new Color(245, 245, 245); // Fondo gris muy suave
    public static final Color TEXT_BLACK  = Color.BLACK;
    public static final Color TEXT_WHITE  = Color.WHITE;
    public static final Color SELECTION   = new Color(180, 200, 230); // Azul claro selección tabla

    public static void install() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        
        // Ajustes generales
        UIManager.put("Panel.background", BG_LIGHT);
        UIManager.put("Label.foreground", TEXT_BLACK);
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 12));
        
        // Tabla estilo "Excel suave"
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", TEXT_BLACK);
        UIManager.put("Table.selectionBackground", SELECTION);
        UIManager.put("Table.selectionForeground", TEXT_BLACK);
        UIManager.put("Table.gridColor", Color.LIGHT_GRAY);
        UIManager.put("TableHeader.background", BLUE_HEADER);
        UIManager.put("TableHeader.foreground", TEXT_WHITE);
        
        UIManager.put("OptionPane.background", BG_LIGHT);
        UIManager.put("OptionPane.messageForeground", TEXT_BLACK);
    }

    // Crea un panel de cabecera azul como el de la imagen "Buscar..." o "Selección..."
    public static JPanel createBlueHeader(String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(BLUE_HEADER);
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(TEXT_WHITE);
        p.add(l);
        return p;
    }

    // Campos de texto simples
    public static void styleTextField(JTextField f) {
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT_BLACK);
        f.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        f.setPreferredSize(new Dimension(150, 24));
    }

    public static void forceTableHeaderStyle(JTable t) {
        JTableHeader h = t.getTableHeader();
        h.setBackground(BLUE_HEADER);
        h.setForeground(TEXT_WHITE);
        h.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    // Botones con la flecha roja ▶ típica de la imagen
    public static JButton createBtn(String text) {
        JButton b = new JButton(text);
        b.setIcon(new Icon() { // Pequeño icono rojo pintado a mano
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(Color.RED);
                g.fillOval(0, 0, 14, 14); // Círculo rojo
                g.setColor(Color.WHITE);
                int[] xs = {4, 4, 10}; int[] ys = {3, 11, 7}; // Triángulo play
                g.fillPolygon(xs, ys, 3);
            }
            public int getIconWidth() { return 16; }
            public int getIconHeight() { return 16; }
        });
        b.setHorizontalTextPosition(SwingConstants.LEFT); // Texto a la izquierda del icono
        b.setBackground(Color.WHITE); // Botón blanco simple? O transparente?
        b.setContentAreaFilled(false); // Estilo "Link" o botón plano
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setForeground(TEXT_BLACK);
        b.setFont(new Font("SansSerif", Font.BOLD, 11));
        return b;
    }
}