package ui;

import model.Flor;
import persistence.FlorDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FloresPanel extends JPanel {
    private FlorDAO dao = new FlorDAO();
    private JTextField txtId = new JTextField(5);
    private JTextField txtNombre = new JTextField(15);
    private JTable table;
    private DefaultTableModel model;

    public FloresPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // --- 1. Panel Búsqueda (Arriba) ---
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBackground(UiTheme.SURFACE);
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UiTheme.ACCENT), "Buscar Flores", 0,0, null, UiTheme.ACCENT));
        
        UiTheme.styleTextField(txtId);
        UiTheme.styleTextField(txtNombre);
        JButton btnBuscar = UiTheme.createBtn("Buscar", UiTheme.ACCENT, Color.BLACK);
        JButton btnLimpiar = UiTheme.createBtn("Limpiar", UiTheme.DANGER, Color.WHITE);

        pnlBusqueda.add(new JLabel("ID:")); pnlBusqueda.add(txtId);
        pnlBusqueda.add(new JLabel("Nombre:")); pnlBusqueda.add(txtNombre);
        pnlBusqueda.add(btnBuscar); pnlBusqueda.add(btnLimpiar);

        add(pnlBusqueda, BorderLayout.NORTH);

        // --- 2. Tabla (Centro) ---
        String[] cols = {"ID", "Nombre", "Color"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        UiTheme.forceTableHeaderStyle(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UiTheme.ACCENT), "Selección de Flores", 0,0, null, UiTheme.ACCENT));
        scroll.getViewport().setBackground(UiTheme.SURFACE);
        
        add(scroll, BorderLayout.CENTER);

        // --- 3. Botones (Abajo) ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBotones.setBackground(UiTheme.BG_DARK);
        JButton btnAlta = UiTheme.createBtn("Alta", UiTheme.ACCENT, Color.BLACK);
        JButton btnCons = UiTheme.createBtn("Consultar/Borrar", UiTheme.NEUTRAL, Color.BLACK); // Consultar y Borrar juntos
        JButton btnMod = UiTheme.createBtn("Modificar", UiTheme.NEUTRAL, Color.BLACK);

        pnlBotones.add(btnAlta); pnlBotones.add(btnCons); pnlBotones.add(btnMod);
        add(pnlBotones, BorderLayout.SOUTH);

        // --- Eventos ---
        cargarTabla(dao.listar());

        btnBuscar.addActionListener(e -> cargarTabla(dao.buscar(txtNombre.getText())));
        btnLimpiar.addActionListener(e -> { txtNombre.setText(""); cargarTabla(dao.listar()); });

        btnAlta.addActionListener(e -> {
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Nueva Flor", null);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnCons.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;
            int id = (int) table.getValueAt(row, 0);
            // Opción de borrar aquí para simplificar
            int opt = JOptionPane.showConfirmDialog(this, "¿Borrar Flor ID " + id + "? (Cuidado FK)", "Consultar", JOptionPane.YES_NO_OPTION);
            if(opt == JOptionPane.YES_OPTION) {
                try {
                    dao.borrar(id);
                    cargarTabla(dao.listar());
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al borrar (¿Tiene Pedidos?): " + ex.getMessage());
                }
            }
        });
        
        btnMod.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;
            int id = (int) table.getValueAt(row, 0);
            Flor f = dao.listar().stream().filter(fl -> fl.getIdFlor() == id).findFirst().orElse(null);
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Modificar", f);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });
    }

    private void cargarTabla(List<Flor> lista) {
        model.setRowCount(0);
        for(Flor f : lista) model.addRow(new Object[]{f.getIdFlor(), f.getNombreFlor(), f.getColor()});
    }
}