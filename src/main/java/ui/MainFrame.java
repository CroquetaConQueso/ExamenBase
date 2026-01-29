package ui;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Examen Carlos Torres - Acceso a Datos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gestión Flores", new FloresPanel());
        // Aquí podrías añadir un PedidosPanel similar si da tiempo
        tabs.addTab("Pedidos (Opcional)", new JPanel()); 
        
        add(tabs);
    }
}