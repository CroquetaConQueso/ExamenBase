package ui;

import model.Pedido;
import persistence.PedidoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PedidosPanel extends JPanel {
    private PedidoDAO dao = new PedidoDAO();
    private JTextField txtFlor = new JTextField(15);
    private JTable table;
    private DefaultTableModel model;

    public PedidosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // --- 1. Filtros ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(UiTheme.SURFACE);
        top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UiTheme.ACCENT), "Buscar Pedidos", 0,0, null, UiTheme.ACCENT));
        
        UiTheme.styleTextField(txtFlor);
        JButton btnBuscar = UiTheme.createBtn("Filtrar por Flor", UiTheme.ACCENT, Color.BLACK);
        JButton btnRefrescar = UiTheme.createBtn("Refrescar", UiTheme.NEUTRAL, Color.BLACK);

        top.add(new JLabel("Nombre Flor:")); top.add(txtFlor);
        top.add(btnBuscar); top.add(btnRefrescar);

        add(top, BorderLayout.NORTH);

        // --- 2. Tabla ---
        String[] cols = {"ID Pedido", "Flor", "Cantidad"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        UiTheme.forceTableHeaderStyle(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UiTheme.SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(UiTheme.ACCENT));
        
        add(scroll, BorderLayout.CENTER);

        // --- 3. Botones ---
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bot.setBackground(UiTheme.BG_DARK);
        JButton btnAdd = UiTheme.createBtn("Nuevo Pedido", UiTheme.ACCENT, Color.BLACK);
        JButton btnDel = UiTheme.createBtn("Borrar Seleccionado", UiTheme.DANGER, Color.WHITE);
        
        bot.add(btnAdd); bot.add(btnDel);
        add(bot, BorderLayout.SOUTH);

        // --- Lógica ---
        cargarTabla(dao.listar());

        btnRefrescar.addActionListener(e -> { txtFlor.setText(""); cargarTabla(dao.listar()); });
        btnBuscar.addActionListener(e -> cargarTabla(dao.buscar(txtFlor.getText())));

        btnAdd.addActionListener(e -> {
            // Abre el JDialog modal
            PedidoDialog d = new PedidoDialog(SwingUtilities.getWindowAncestor(this));
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnDel.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r == -1) { JOptionPane.showMessageDialog(this, "Selecciona un pedido."); return; }
            int id = (int) table.getValueAt(r, 0);
            
            if(JOptionPane.showConfirmDialog(this, "¿Borrar pedido " + id + "?") == JOptionPane.YES_OPTION) {
                try {
                    dao.borrar(id);
                    cargarTabla(dao.listar());
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
    }

    private void cargarTabla(List<Pedido> l) {
        model.setRowCount(0);
        for(Pedido p : l) {
            String nombreFlor = (p.getFlor() != null) ? p.getFlor().getNombreFlor() : "---";
            model.addRow(new Object[]{ p.getIdPedido(), nombreFlor, p.getCantidad() });
        }
    }
}