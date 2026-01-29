package ui;

import model.Flor;
import model.Pedido;
import persistence.FlorDAO;
import persistence.PedidoDAO;
import javax.swing.*;
import java.awt.*;

public class PedidoDialog extends JDialog {
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private FlorDAO florDAO = new FlorDAO();
    
    private JTextField txtId = new JTextField(10);
    private JTextField txtCant = new JTextField(10);
    private JComboBox<Flor> cbFlor = new JComboBox<>();

    public PedidoDialog(Window owner) {
        super(owner, "Nuevo Pedido", ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBackground(UiTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Cargar Flores en el Combo
        try {
            for(Flor f : florDAO.listar()) cbFlor.addItem(f);
        } catch(Exception e) {}

        UiTheme.styleTextField(txtId);
        UiTheme.styleTextField(txtCant);

        p.add(new JLabel("ID Pedido:")); p.add(txtId);
        p.add(new JLabel("Flor:"));      p.add(cbFlor);
        p.add(new JLabel("Cantidad:"));  p.add(txtCant);

        JButton btnSave = UiTheme.createBtn("Guardar", UiTheme.ACCENT, Color.BLACK);
        p.add(new JLabel("")); p.add(btnSave);

        add(p);

        btnSave.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                int cant = Integer.parseInt(txtCant.getText());
                Flor florSel = (Flor) cbFlor.getSelectedItem();
                
                if(florSel == null) throw new Exception("Debes seleccionar una flor.");

                Pedido nuevo = new Pedido(id, florSel, cant);
                pedidoDAO.insertar(nuevo);
                dispose();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}