package ui;

import model.Distribuidor;
import model.Flor;
import persistence.DistribuidorDAO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DistribuidoresPanel extends JPanel {
    private DistribuidorDAO dao = new DistribuidorDAO();
    
    private JTextField txtCif = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JCheckBox chkSelectAll = new JCheckBox("Seleccionar todo");
    
    private JTable table;
    private DistTableModel model = new DistTableModel();
    private JLabel lblTotal = new JLabel("Registros: 0");

    public DistribuidoresPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);

        // --- NORTE ---
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.add(UiTheme.createBlueHeader("Buscar Distribuidores"));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(UiTheme.BG_LIGHT);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 5, 5, 15);

        // Filtros CIF y Nombre (Como en la imagen del PDF)
        g.gridx = 0; g.gridy = 0; pnlForm.add(new JLabel("CIF:"), g);
        g.gridx = 1; txtCif.setPreferredSize(new Dimension(100, 24)); UiTheme.styleTextField(txtCif); pnlForm.add(txtCif, g);

        g.gridx = 2; pnlForm.add(new JLabel("Razón Social:"), g);
        g.gridx = 3; g.weightx = 1.0; txtNombre.setPreferredSize(new Dimension(250, 24)); UiTheme.styleTextField(txtNombre); pnlForm.add(txtNombre, g);

        g.gridx = 0; g.gridy = 1; g.gridwidth = 4; g.weightx = 0.0; g.insets = new Insets(10, 5, 5, 5);
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBtn.setBackground(UiTheme.BG_LIGHT);
        JButton btnBuscar = UiTheme.createBtn("Buscar");
        JButton btnRefrescar = UiTheme.createBtn("Limpiar");
        pBtn.add(btnBuscar); pBtn.add(Box.createHorizontalStrut(15)); pBtn.add(btnRefrescar); pBtn.add(Box.createHorizontalStrut(15)); pBtn.add(chkSelectAll);
        pnlForm.add(pBtn, g);
        pnlNorte.add(pnlForm);
        add(pnlNorte, BorderLayout.NORTH);

        // --- CENTRO ---
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(UiTheme.createBlueHeader("Selección de Empresas Distribuidoras"), BorderLayout.NORTH);
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
        JButton btnBaja = UiTheme.createBtn("Borrar Marcados");
        JButton btnRel = UiTheme.createBtn("Ver Flores (N:M)"); // Botón Especial N:M

        pnlSur.add(btnAlta); pnlSur.add(btnCons); pnlSur.add(btnMod); pnlSur.add(btnBaja); pnlSur.add(btnRel);
        add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        cargarTabla(dao.listar());

        btnRefrescar.addActionListener(e -> { txtCif.setText(""); txtNombre.setText(""); cargarTabla(dao.listar()); });
        btnBuscar.addActionListener(e -> {
            List<Distribuidor> res = dao.buscar(txtNombre.getText());
            if(!txtCif.getText().isEmpty()) {
                res = res.stream().filter(d -> d.getCif().toUpperCase().contains(txtCif.getText().toUpperCase())).collect(Collectors.toList());
            }
            cargarTabla(res);
        });
        chkSelectAll.addActionListener(e -> { model.seleccionarTodo(chkSelectAll.isSelected()); table.repaint(); });

        btnAlta.addActionListener(e -> {
            DistribuidorDialog d = new DistribuidorDialog(SwingUtilities.getWindowAncestor(this), null, false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnMod.addActionListener(e -> {
            List<Distribuidor> sel = model.getSeleccionados();
            if(sel.size()!=1) { JOptionPane.showMessageDialog(this,"Seleccione UN registro."); return; }
            DistribuidorDialog d = new DistribuidorDialog(SwingUtilities.getWindowAncestor(this), sel.get(0), false);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnCons.addActionListener(e -> {
            List<Distribuidor> sel = model.getSeleccionados();
            if(sel.size()!=1) { JOptionPane.showMessageDialog(this,"Seleccione UN registro."); return; }
            DistribuidorDialog d = new DistribuidorDialog(SwingUtilities.getWindowAncestor(this), sel.get(0), true);
            d.setVisible(true);
        });
        
        // REQUISITO: Ver datos relacionados (N:M)
        btnRel.addActionListener(e -> {
            List<Distribuidor> sel = model.getSeleccionados();
            if(sel.size()!=1) { JOptionPane.showMessageDialog(this,"Seleccione UN distribuidor."); return; }
            
            Distribuidor d = sel.get(0);
            String msg = "Flores distribuidas por " + d.getNombre() + ":\n";
            if(d.getFlores().isEmpty()) msg += "(Ninguna)";
            else for(Flor f : d.getFlores()) msg += "- " + f.getNombreFlor() + " (" + f.getColor() + ")\n";
            
            JOptionPane.showMessageDialog(this, msg);
        });

        btnBaja.addActionListener(e -> {
            List<Distribuidor> sel = model.getSeleccionados();
            if(sel.isEmpty()) return;
            
            JPanel pnlConfirm = new JPanel(new BorderLayout());
            pnlConfirm.add(new JLabel("¿Eliminar " + sel.size() + " distribuidores?"), BorderLayout.NORTH);
            DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Nombre", "CIF"}, 0);
            for(Distribuidor d : sel) tm.addRow(new Object[]{d.getIdDistribuidor(), d.getNombre(), d.getCif()});
            JTable t = new JTable(tm); UiTheme.forceTableHeaderStyle(t);
            pnlConfirm.add(new JScrollPane(t), BorderLayout.CENTER);
            
            if(JOptionPane.showConfirmDialog(this, pnlConfirm, "Confirmar", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                try { for(Distribuidor d : sel) dao.borrar(d.getIdDistribuidor()); cargarTabla(dao.listar()); }
                catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
    }

    private void cargarTabla(List<Distribuidor> l) {
        model.setDatos(l); chkSelectAll.setSelected(false); lblTotal.setText("Registros: " + l.size());
    }

    private static class DistTableModel extends AbstractTableModel {
        private final String[] cols = {"Sel", "ID", "Nombre", "CIF"};
        private List<Fila> filas = new ArrayList<>();
        private static class Fila { boolean sel; Distribuidor d; Fila(Distribuidor d){this.d=d;} }
        public void setDatos(List<Distribuidor> l) { filas.clear(); for(Distribuidor d:l) filas.add(new Fila(d)); fireTableDataChanged(); }
        public void seleccionarTodo(boolean s) { for(Fila f:filas) f.sel=s; }
        public List<Distribuidor> getSeleccionados() { List<Distribuidor> r=new ArrayList<>(); for(Fila f:filas) if(f.sel) r.add(f.d); return r; }
        public int getRowCount() { return filas.size(); }
        public int getColumnCount() { return cols.length; }
        public String getColumnName(int i) { return cols[i]; }
        public Class<?> getColumnClass(int i) { return i==0?Boolean.class:String.class; }
        public boolean isCellEditable(int r, int c) { return c==0; }
        public Object getValueAt(int r, int c) {
            Fila f = filas.get(r);
            switch(c) { case 0: return f.sel; case 1: return f.d.getIdDistribuidor(); case 2: return f.d.getNombre(); case 3: return f.d.getCif(); default: return null; }
        }
        public void setValueAt(Object v, int r, int c) { if(c==0) { filas.get(r).sel=(boolean)v; fireTableCellUpdated(r,c); } }
    }
}