package ui;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Examen Carlos Torres - Acceso a Datos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gestión Flores", new FloresPanel());
        tabs.addTab("Gestión Pedidos", new PedidosPanel()); // <--- AÑADIDO
        
        add(tabs);
    }
}