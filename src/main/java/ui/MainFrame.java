package ui;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Mantenimiento de Flores y Pedidos"); // Título ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        
        // Cabecera Principal del Programa (Estilo "Mantenimiento de Empresas...")
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UiTheme.BLUE_HEADER));
        JLabel lblTitle = new JLabel("Mantenimiento de Flores y Pedidos (Examen Carlos Torres)");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(UiTheme.BLUE_HEADER);
        header.add(lblTitle);
        
        add(header, BorderLayout.NORTH);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Flores", new FloresPanel());
        tabs.addTab("Pedidos", new PedidosPanel());
        
        add(tabs, BorderLayout.CENTER);
    }
}