package ui;

import model.Flor;
import model.Pedido;
import persistence.FlorDAO;
import persistence.PedidoDAO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FloresPanel extends JPanel {
    private FlorDAO dao = new FlorDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    
    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JCheckBox chkSelectAll = new JCheckBox("Seleccionar todo");
    private JTable table;
    private FloresTableModel model = new FloresTableModel();
    private JLabel lblTotal = new JLabel("Registros: 0");

    public FloresPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);

        // --- NORTE ---
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.add(UiTheme.createBlueHeader("Buscar Flores"));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(UiTheme.BG_LIGHT);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 5, 5, 15);

        g.gridx = 0; g.gridy = 0; pnlForm.add(new JLabel("ID Flor:"), g);
        g.gridx = 1; txtId.setPreferredSize(new Dimension(100, 24)); UiTheme.styleTextField(txtId); pnlForm.add(txtId, g);

        g.gridx = 2; pnlForm.add(new JLabel("Nombre:"), g);
        g.gridx = 3; g.weightx = 1.0; txtNombre.setPreferredSize(new Dimension(250, 24)); UiTheme.styleTextField(txtNombre); pnlForm.add(txtNombre, g);

        g.gridx = 0; g.gridy = 1; g.gridwidth = 4; g.weightx = 0.0; g.insets = new Insets(10, 5, 5, 5);
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBtn.setBackground(UiTheme.BG_LIGHT);
        JButton btnBuscar = UiTheme.createBtn("Buscar");
        JButton btnLimpiar = UiTheme.createBtn("Limpiar");
        pBtn.add(btnBuscar); pBtn.add(Box.createHorizontalStrut(15)); pBtn.add(btnLimpiar); pBtn.add(Box.createHorizontalStrut(15)); pBtn.add(chkSelectAll);
        pnlForm.add(pBtn, g);
        pnlNorte.add(pnlForm);
        add(pnlNorte, BorderLayout.NORTH);

        // --- CENTRO ---
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(UiTheme.createBlueHeader("Selección de Flores"), BorderLayout.NORTH);
        table = new JTable(model);
        table.setRowHeight(22);
        UiTheme.forceTableHeaderStyle(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40); 
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        pnlCentro.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFooter.setBackground(UiTheme.BG_LIGHT);
        pnlFooter.add(lblTotal);
        pnlCentro.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlCentro, BorderLayout.CENTER);

        // --- SUR ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSur.setBackground(UiTheme.BG_LIGHT);
        pnlSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton btnAlta = UiTheme.createBtn("Alta");
        JButton btnCons = UiTheme.createBtn("Consultar");
        JButton btnMod = UiTheme.createBtn("Modificar");
        JButton btnEliminar = UiTheme.createBtn("Eliminar");
        JButton btnRel = UiTheme.createBtn("Ver Pedidos Asociados");

        pnlSur.add(btnAlta); pnlSur.add(btnCons); pnlSur.add(btnMod); pnlSur.add(btnEliminar); pnlSur.add(btnRel);
        add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        cargarTabla(dao.listar());

        btnBuscar.addActionListener(e -> {
            String idS = txtId.getText().trim();
            if(!idS.isEmpty()) {
                try { 
                    int id = Integer.parseInt(idS); 
                    cargarTabla(dao.listar().stream().filter(f->f.getIdFlor()==id).collect(Collectors.toList())); 
                } catch(Exception ex) { JOptionPane.showMessageDialog(this,"ID Inválido"); }
            } else {
                cargarTabla(dao.buscar(txtNombre.getText()));
            }
        });

        btnLimpiar.addActionListener(e -> { txtNombre.setText(""); txtId.setText(""); cargarTabla(dao.listar()); });
        chkSelectAll.addActionListener(e -> { model.seleccionarTodo(chkSelectAll.isSelected()); table.repaint(); });

        btnAlta.addActionListener(e -> {
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Nueva Flor", null, false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnMod.addActionListener(e -> {
            List<Flor> s = model.getSeleccionados();
            if(s.size()!=1) { JOptionPane.showMessageDialog(this,"Selecciona UNA flor."); return; }
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Modificar", s.get(0), false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnCons.addActionListener(e -> {
            List<Flor> s = model.getSeleccionados();
            if(s.size()!=1) { JOptionPane.showMessageDialog(this,"Selecciona UNA flor."); return; }
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Consultar", s.get(0), true);
            d.setVisible(true);
        });

        btnRel.addActionListener(e -> {
            List<Flor> s = model.getSeleccionados();
            if(s.size()!=1) { JOptionPane.showMessageDialog(this,"Selecciona UNA flor."); return; }
            Flor f = s.get(0);
            List<Pedido> pedidos = pedidoDAO.listar().stream()
                .filter(p -> p.getFlor().getIdFlor() == f.getIdFlor()).collect(Collectors.toList());
            if(pedidos.isEmpty()) JOptionPane.showMessageDialog(this, "La flor '" + f.getNombreFlor() + "' no tiene pedidos.");
            else {
                String msj = "Pedidos de " + f.getNombreFlor() + ":\n";
                for(Pedido p : pedidos) msj += "- Pedido " + p.getIdPedido() + " (" + p.getCantidad() + " uds)\n";
                JOptionPane.showMessageDialog(this, msj);
            }
        });

        // --- BOTÓN ELIMINAR CON TABLA DE CONFIRMACIÓN ---
        btnEliminar.addActionListener(e -> {
            List<Flor> s = model.getSeleccionados();
            if(s.isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona al menos una flor."); return; }

            // 1. Crear Panel con Tabla para el Dialog
            JPanel pnlConfirm = new JPanel(new BorderLayout());
            pnlConfirm.add(new JLabel("¿Desea ELIMINAR permanentemente estos " + s.size() + " registros?"), BorderLayout.NORTH);
            pnlConfirm.add(new JLabel("(Se borrarán también sus pedidos asociados)"), BorderLayout.SOUTH);
            
            // Modelo temporal simple
            DefaultTableModel tempModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Color"}, 0);
            for(Flor f : s) tempModel.addRow(new Object[]{f.getIdFlor(), f.getNombreFlor(), f.getColor()});
            
            JTable tempTable = new JTable(tempModel);
            UiTheme.forceTableHeaderStyle(tempTable);
            JScrollPane scroll = new JScrollPane(tempTable);
            scroll.setPreferredSize(new Dimension(400, 150));
            pnlConfirm.add(scroll, BorderLayout.CENTER);

            // 2. Mostrar Dialog
            int opt = JOptionPane.showConfirmDialog(this, pnlConfirm, "Confirmar Borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if(opt == JOptionPane.YES_OPTION) {
                try { 
                    for(Flor f : s) dao.borrar(f.getIdFlor()); // Ahora usa el borrado en cascada del DAO
                    cargarTabla(dao.listar()); 
                }
                catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
    }

    private void cargarTabla(List<Flor> lista) {
        model.setDatos(lista); chkSelectAll.setSelected(false); lblTotal.setText("Número total de registros: " + lista.size());
    }

    private static class FloresTableModel extends AbstractTableModel {
        private final String[] columnas = {"Sel", "ID", "Nombre", "Color"};
        private List<Fila> filas = new ArrayList<>();
        private static class Fila { boolean sel; Flor flor; Fila(Flor f) { this.flor = f; } }
        public void setDatos(List<Flor> flores) { filas.clear(); for(Flor f : flores) filas.add(new Fila(f)); fireTableDataChanged(); }
        public void seleccionarTodo(boolean sel) { for(Fila f : filas) f.sel = sel; }
        public List<Flor> getSeleccionados() { List<Flor> l = new ArrayList<>(); for(Fila f : filas) if(f.sel) l.add(f.flor); return l; }
        @Override public int getRowCount() { return filas.size(); }
        @Override public int getColumnCount() { return columnas.length; }
        @Override public String getColumnName(int c) { return columnas[c]; }
        @Override public Class<?> getColumnClass(int c) { return c==0 ? Boolean.class : String.class; }
        @Override public boolean isCellEditable(int r, int c) { return c==0; }
        @Override public Object getValueAt(int r, int c) {
            Fila f = filas.get(r);
            switch(c) { case 0: return f.sel; case 1: return f.flor.getIdFlor(); case 2: return f.flor.getNombreFlor(); case 3: return f.flor.getColor(); default: return null; }
        }
        @Override public void setValueAt(Object v, int r, int c) { if(c==0) { filas.get(r).sel = (boolean)v; fireTableCellUpdated(r,c); } }
    }
}