package ui;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Mantenimiento de Flores, Pedidos y Distribuidores"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700); 
        setLocationRelativeTo(null);
        
        // Cabecera
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UiTheme.BLUE_HEADER));
        
        JLabel lblTitle = new JLabel("Mantenimiento Integral (Examen Carlos Torres)");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(UiTheme.BLUE_HEADER);
        header.add(lblTitle);
        
        add(header, BorderLayout.NORTH);
        
        // Pestañas
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Flores", new FloresPanel());
        tabs.addTab("Pedidos", new PedidosPanel());
        
        // CAMBIO AQUÍ: Nombre más profesional, sin el "(N:M)"
        tabs.addTab("Empresas Distribuidoras", new DistribuidoresPanel()); 
        
        add(tabs, BorderLayout.CENTER);
    }
}