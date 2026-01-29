package ui;

import model.Pedido;
import persistence.PedidoDAO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidosPanel extends JPanel {
    private PedidoDAO dao = new PedidoDAO();
    
    private JTextField txtFlor = new JTextField();
    private JTextField txtCantidad = new JTextField(); 
    private JTextField txtIdFlor = new JTextField();
    
    private JCheckBox chkSelectAll = new JCheckBox("Seleccionar todo");
    private JTable table;
    private PedidosTableModel model = new PedidosTableModel();
    private JLabel lblTotal = new JLabel("Registros: 0");

    public PedidosPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);

        // --- FILTROS ---
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.add(UiTheme.createBlueHeader("Buscar Pedidos"));
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(UiTheme.BG_LIGHT);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 5, 5, 15);

        g.gridx = 0; g.gridy = 0; pnlForm.add(new JLabel("Nombre Flor:"), g);
        g.gridx = 1; g.weightx = 0.5; g.fill = GridBagConstraints.HORIZONTAL;
        txtFlor.setPreferredSize(new Dimension(150, 24)); UiTheme.styleTextField(txtFlor); pnlForm.add(txtFlor, g);

        g.gridx = 2; g.weightx = 0.0; g.fill = GridBagConstraints.NONE; pnlForm.add(new JLabel("Cantidad:"), g);
        g.gridx = 3; g.weightx = 0.5; g.fill = GridBagConstraints.HORIZONTAL;
        txtCantidad.setPreferredSize(new Dimension(80, 24)); UiTheme.styleTextField(txtCantidad); pnlForm.add(txtCantidad, g);

        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE; pnlForm.add(new JLabel("ID Flor (FK):"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        txtIdFlor.setPreferredSize(new Dimension(100, 24)); UiTheme.styleTextField(txtIdFlor); pnlForm.add(txtIdFlor, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; g.weightx = 0.0; g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(10, 5, 5, 5);
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBtns.setBackground(UiTheme.BG_LIGHT);
        JButton btnBuscar = UiTheme.createBtn("Filtrar");
        JButton btnRefrescar = UiTheme.createBtn("Refrescar");
        pBtns.add(btnBuscar); pBtns.add(Box.createHorizontalStrut(15)); pBtns.add(btnRefrescar); pBtns.add(Box.createHorizontalStrut(15)); pBtns.add(chkSelectAll);
        pnlForm.add(pBtns, g);
        pnlNorte.add(pnlForm);
        add(pnlNorte, BorderLayout.NORTH);

        // --- TABLA ---
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(UiTheme.createBlueHeader("Selección de Pedidos"), BorderLayout.NORTH);
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

        // --- CRUD ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSur.setBackground(UiTheme.BG_LIGHT);
        pnlSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JButton btnAlta = UiTheme.createBtn("Nuevo");
        JButton btnCons = UiTheme.createBtn("Consultar");
        JButton btnMod = UiTheme.createBtn("Modificar");
        JButton btnBaja = UiTheme.createBtn("Borrar Marcados");
        pnlSur.add(btnAlta); pnlSur.add(btnCons); pnlSur.add(btnMod); pnlSur.add(btnBaja);
        add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        cargarTabla(dao.listar());
        
        btnRefrescar.addActionListener(e -> { txtFlor.setText(""); txtCantidad.setText(""); txtIdFlor.setText(""); cargarTabla(dao.listar()); });
        btnBuscar.addActionListener(e -> {
            List<Pedido> res = dao.listar();
            if(!txtFlor.getText().isEmpty()) res = dao.buscar(txtFlor.getText());
            String cantTxt = txtCantidad.getText().trim();
            if(!cantTxt.isEmpty()) { try { int c = Integer.parseInt(cantTxt); res = res.stream().filter(p -> p.getCantidad() == c).collect(Collectors.toList()); } catch(Exception ex) {} }
            String idFlorTxt = txtIdFlor.getText().trim();
            if(!idFlorTxt.isEmpty()) { try { int idF = Integer.parseInt(idFlorTxt); res = res.stream().filter(p -> p.getFlor() != null && p.getFlor().getIdFlor() == idF).collect(Collectors.toList()); } catch(Exception ex) {} }
            cargarTabla(res);
        });
        chkSelectAll.addActionListener(e -> { model.seleccionarTodo(chkSelectAll.isSelected()); table.repaint(); });

        btnAlta.addActionListener(e -> {
            PedidoDialog d = new PedidoDialog(SwingUtilities.getWindowAncestor(this), null, false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnMod.addActionListener(e -> {
            List<Pedido> sel = model.getSeleccionados();
            if(sel.size() != 1) { JOptionPane.showMessageDialog(this, "Selecciona UN registro para modificar."); return; }
            PedidoDialog d = new PedidoDialog(SwingUtilities.getWindowAncestor(this), sel.get(0), false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnCons.addActionListener(e -> {
            List<Pedido> sel = model.getSeleccionados();
            if(sel.size() != 1) { JOptionPane.showMessageDialog(this, "Selecciona UN registro."); return; }
            PedidoDialog d = new PedidoDialog(SwingUtilities.getWindowAncestor(this), sel.get(0), true);
            d.setVisible(true);
        });

        // --- BOTÓN BORRAR CON TABLA CONFIRMACIÓN ---
        btnBaja.addActionListener(e -> {
            List<Pedido> sel = model.getSeleccionados();
            if(sel.isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona al menos un pedido."); return; }
            
            // Tabla temporal confirmación
            JPanel pnlConfirm = new JPanel(new BorderLayout());
            pnlConfirm.add(new JLabel("¿Eliminar estos " + sel.size() + " pedidos?"), BorderLayout.NORTH);
            DefaultTableModel tempModel = new DefaultTableModel(new String[]{"ID", "Flor", "Cantidad"}, 0);
            for(Pedido p : sel) tempModel.addRow(new Object[]{p.getIdPedido(), (p.getFlor()!=null?p.getFlor().getNombreFlor():"-"), p.getCantidad()});
            
            JTable tempTable = new JTable(tempModel);
            UiTheme.forceTableHeaderStyle(tempTable);
            JScrollPane scroll = new JScrollPane(tempTable);
            scroll.setPreferredSize(new Dimension(400, 150));
            pnlConfirm.add(scroll, BorderLayout.CENTER);

            int opt = JOptionPane.showConfirmDialog(this, pnlConfirm, "Confirmar Borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(opt == JOptionPane.YES_OPTION) {
                try { for(Pedido p : sel) dao.borrar(p.getIdPedido()); cargarTabla(dao.listar()); } 
                catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
    }

    private void cargarTabla(List<Pedido> l) {
        model.setDatos(l); chkSelectAll.setSelected(false); lblTotal.setText("Registros: " + l.size());
    }

    private static class PedidosTableModel extends AbstractTableModel {
        private final String[] cols = {"Sel", "ID", "Flor", "Cantidad"};
        private List<Fila> filas = new ArrayList<>();
        private static class Fila { boolean sel; Pedido p; Fila(Pedido p){this.p=p;} }
        public void setDatos(List<Pedido> l) { filas.clear(); for(Pedido p:l) filas.add(new Fila(p)); fireTableDataChanged(); }
        public void seleccionarTodo(boolean s) { for(Fila f:filas) f.sel=s; }
        public List<Pedido> getSeleccionados() { List<Pedido> r = new ArrayList<>(); for(Fila f:filas) if(f.sel) r.add(f.p); return r; }
        @Override public int getRowCount() { return filas.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int i) { return cols[i]; }
        @Override public Class<?> getColumnClass(int i) { return i==0?Boolean.class:String.class; }
        @Override public boolean isCellEditable(int r, int c) { return c==0; }
        @Override public Object getValueAt(int r, int c) {
            Fila f = filas.get(r);
            switch(c) { case 0: return f.sel; case 1: return f.p.getIdPedido(); case 2: return (f.p.getFlor()!=null?f.p.getFlor().getNombreFlor():"-"); case 3: return f.p.getCantidad(); default: return null; }
        }
        @Override public void setValueAt(Object v, int r, int c) { if(c==0) { filas.get(r).sel=(boolean)v; fireTableCellUpdated(r,c); } }
    }
}