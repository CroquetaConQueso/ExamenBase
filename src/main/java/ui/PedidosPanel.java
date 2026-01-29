package ui;

import model.Pedido;
import persistence.PedidoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PedidosPanel extends JPanel {
    private PedidoDAO dao = new PedidoDAO();
    private JTextField txtFlor = new JTextField();
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal = new JLabel("Registros: 0");

    public PedidosPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);

        // ZONA SUPERIOR
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.add(UiTheme.createBlueHeader("Buscar Pedidos"));

        // Formulario Horizontal con GridBagLayout
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(UiTheme.BG_LIGHT);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 5, 5, 15);

        // Fila 1: Filtro
        g.gridx = 0; g.gridy = 0;
        pnlForm.add(new JLabel("Nombre Flor:"), g);
        
        g.gridx = 1; g.weightx = 1.0;
        txtFlor.setPreferredSize(new Dimension(200, 24));
        UiTheme.styleTextField(txtFlor);
        pnlForm.add(txtFlor, g);

        // Fila 2: Botones
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; g.weightx = 0.0;
        g.insets = new Insets(10, 5, 5, 5);
        
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBtns.setBackground(UiTheme.BG_LIGHT);
        JButton btnBuscar = UiTheme.createBtn("Filtrar");
        JButton btnRefrescar = UiTheme.createBtn("Refrescar");
        pBtns.add(btnBuscar); pBtns.add(Box.createHorizontalStrut(15)); pBtns.add(btnRefrescar);
        
        pnlForm.add(pBtns, g);

        pnlNorte.add(pnlForm);
        add(pnlNorte, BorderLayout.NORTH);

        // ZONA CENTRAL
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(UiTheme.createBlueHeader("Selección de Pedidos"), BorderLayout.NORTH);

        String[] cols = {"ID Pedido", "Flor", "Cantidad"};
        model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        table.setRowHeight(22);
        UiTheme.forceTableHeaderStyle(table);
        
        pnlCentro.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFooter.setBackground(UiTheme.BG_LIGHT);
        pnlFooter.add(lblTotal);
        pnlCentro.add(pnlFooter, BorderLayout.SOUTH);
        
        add(pnlCentro, BorderLayout.CENTER);

        // ZONA INFERIOR
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSur.setBackground(UiTheme.BG_LIGHT);
        pnlSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton btnAlta = UiTheme.createBtn("Nuevo Pedido");
        JButton btnBaja = UiTheme.createBtn("Borrar Pedido");
        pnlSur.add(btnAlta); pnlSur.add(btnBaja);
        add(pnlSur, BorderLayout.SOUTH);

        // Lógica
        cargarTabla(dao.listar());
        btnRefrescar.addActionListener(e -> { txtFlor.setText(""); cargarTabla(dao.listar()); });
        btnBuscar.addActionListener(e -> cargarTabla(dao.buscar(txtFlor.getText())));
        btnAlta.addActionListener(e -> {
            PedidoDialog d = new PedidoDialog(SwingUtilities.getWindowAncestor(this));
            d.setVisible(true);
            cargarTabla(dao.listar());
        });
        btnBaja.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r == -1) return;
            int id = (int) table.getValueAt(r, 0);
            if(JOptionPane.showConfirmDialog(this, "¿Borrar pedido " + id + "?") == JOptionPane.YES_OPTION) {
                try { dao.borrar(id); cargarTabla(dao.listar()); } catch(Exception ex) {}
            }
        });
    }

    private void cargarTabla(List<Pedido> l) {
        model.setRowCount(0);
        for(Pedido p : l) model.addRow(new Object[]{p.getIdPedido(), (p.getFlor()!=null?p.getFlor().getNombreFlor():"-"), p.getCantidad()});
        lblTotal.setText("Registros: " + l.size());
    }
}