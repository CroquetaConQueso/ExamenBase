package ui;

import model.Flor;
import persistence.FlorDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FloresPanel extends JPanel {
    private FlorDAO dao = new FlorDAO();
    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal = new JLabel("Número total de registros: 0");

    public FloresPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);

        // ==========================================================
        // ZONA SUPERIOR: BÚSQUEDA (Estilo "Empresas Distribuidoras")
        // ==========================================================
        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.setBackground(UiTheme.BG_LIGHT);
        
        // 1. Cabecera Azul
        pnlNorte.add(UiTheme.createBlueHeader("Buscar Flores"));

        // 2. Contenedor del Formulario (Filtros + Botones)
        JPanel pnlForm = new JPanel(new GridBagLayout()); // GridBag para control total
        pnlForm.setBackground(UiTheme.BG_LIGHT);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST; // Alinear a la izquierda
        g.insets = new Insets(5, 5, 5, 15); // Márgenes entre componentes

        // --- FILA 1: Campos (ID y Nombre en la misma línea) ---
        
        // Campo ID
        g.gridx = 0; g.gridy = 0; 
        pnlForm.add(new JLabel("ID Flor:"), g);
        
        g.gridx = 1; 
        txtId.setPreferredSize(new Dimension(100, 24)); // Tamaño fijo pequeño
        UiTheme.styleTextField(txtId);
        pnlForm.add(txtId, g);

        // Campo Nombre (A la derecha del ID)
        g.gridx = 2; 
        pnlForm.add(new JLabel("Nombre:"), g);
        
        g.gridx = 3; g.weightx = 1.0; // Que ocupe el resto del ancho si estiras
        txtNombre.setPreferredSize(new Dimension(250, 24)); // Más ancho
        UiTheme.styleTextField(txtNombre);
        pnlForm.add(txtNombre, g);

        // --- FILA 2: Botones Buscar/Limpiar ---
        g.gridx = 0; g.gridy = 1; 
        g.gridwidth = 4; // Ocupar todo el ancho de abajo
        g.weightx = 0.0;
        g.insets = new Insets(10, 5, 5, 5); // Un poco más de margen arriba

        JPanel pnlBtnBusq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBtnBusq.setBackground(UiTheme.BG_LIGHT);
        
        JButton btnBuscar = UiTheme.createBtn("Buscar");
        JButton btnLimpiar = UiTheme.createBtn("Limpiar");
        
        // Espacio entre botones
        pnlBtnBusq.add(btnBuscar);
        pnlBtnBusq.add(Box.createHorizontalStrut(15));
        pnlBtnBusq.add(btnLimpiar);
        
        pnlForm.add(pnlBtnBusq, g);

        pnlNorte.add(pnlForm);
        add(pnlNorte, BorderLayout.NORTH);

        // ==========================================================
        // ZONA CENTRAL: TABLA
        // ==========================================================
        JPanel pnlCentro = new JPanel(new BorderLayout());
        
        // Cabecera Azul Tabla
        pnlCentro.add(UiTheme.createBlueHeader("Selección de Flores"), BorderLayout.NORTH);

        String[] cols = {"ID", "Nombre", "Color"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(22);
        UiTheme.forceTableHeaderStyle(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        pnlCentro.add(scroll, BorderLayout.CENTER);

        // Footer Total
        JPanel pnlFooterTabla = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFooterTabla.setBackground(UiTheme.BG_LIGHT);
        pnlFooterTabla.add(lblTotal);
        pnlCentro.add(pnlFooterTabla, BorderLayout.SOUTH);

        add(pnlCentro, BorderLayout.CENTER);

        // ==========================================================
        // ZONA INFERIOR: BOTONES CRUD
        // ==========================================================
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSur.setBackground(UiTheme.BG_LIGHT);
        pnlSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton btnAlta = UiTheme.createBtn("Alta");
        JButton btnCons = UiTheme.createBtn("Consultar");
        JButton btnMod = UiTheme.createBtn("Modificar");

        pnlSur.add(btnAlta); pnlSur.add(btnCons); pnlSur.add(btnMod);
        add(pnlSur, BorderLayout.SOUTH);

        // --- LÓGICA ---
        cargarTabla(dao.listar());

        btnBuscar.addActionListener(e -> cargarTabla(dao.buscar(txtNombre.getText())));
        btnLimpiar.addActionListener(e -> { txtNombre.setText(""); txtId.setText(""); cargarTabla(dao.listar()); });

        btnAlta.addActionListener(e -> {
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Nueva Flor", null);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });

        btnCons.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Seleccione una flor."); return; }
            int id = (int) table.getValueAt(row, 0);
            
            int opt = JOptionPane.showOptionDialog(this, 
                "Detalles de la Flor " + id + "\n¿Desea ELIMINAR este registro?", 
                "Consultar / Borrar", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
                new Object[]{"Eliminar Registro", "Cerrar"}, "Cerrar");

            if(opt == 0) { 
                try { dao.borrar(id); cargarTabla(dao.listar()); } 
                catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage()); }
            }
        });

        btnMod.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1) return;
            int id = (int) table.getValueAt(row, 0);
            Flor f = dao.listar().stream().filter(fl -> fl.getIdFlor() == id).findFirst().orElse(null);
            FlorDialog d = new FlorDialog(SwingUtilities.getWindowAncestor(this), "Modificar Flor", f);
            d.setVisible(true);
            cargarTabla(dao.listar());
        });
    }

    private void cargarTabla(List<Flor> lista) {
        model.setRowCount(0);
        for(Flor f : lista) model.addRow(new Object[]{f.getIdFlor(), f.getNombreFlor(), f.getColor()});
        lblTotal.setText("Número total de registros: " + lista.size());
    }
}