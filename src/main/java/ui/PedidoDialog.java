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
    
    private JTextField txtId = new JTextField();
    private JTextField txtCant = new JTextField();
    private JComboBox<Flor> cbFlor = new JComboBox<>();

    public PedidoDialog(Window owner, Pedido pedido, boolean readOnly) {
        super(owner, (readOnly ? "Consultar Pedido" : (pedido == null ? "Nuevo Pedido" : "Modificar Pedido")), ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBackground(UiTheme.BG_LIGHT);
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        try { for(Flor f : florDAO.listar()) cbFlor.addItem(f); } catch(Exception e) {}

        UiTheme.styleTextField(txtId);
        UiTheme.styleTextField(txtCant);
        cbFlor.setBackground(Color.WHITE);

        p.add(new JLabel("ID Pedido:")); p.add(txtId);
        p.add(new JLabel("Flor:"));      p.add(cbFlor);
        p.add(new JLabel("Cantidad:"));  p.add(txtCant);

        JButton btnSave = UiTheme.createBtn("Guardar");
        
        if(!readOnly) {
            p.add(new JLabel("")); p.add(btnSave);
        }

        add(p);

        if (pedido != null) {
            txtId.setText(String.valueOf(pedido.getIdPedido()));
            txtId.setEditable(false);
            txtCant.setText(String.valueOf(pedido.getCantidad()));
            if(pedido.getFlor() != null) {
                for(int i=0; i<cbFlor.getItemCount(); i++) {
                    if(cbFlor.getItemAt(i).getIdFlor() == pedido.getFlor().getIdFlor()) {
                        cbFlor.setSelectedIndex(i); break;
                    }
                }
            }
        }

        if(readOnly) {
            txtCant.setEditable(false);
            cbFlor.setEnabled(false);
        }

        btnSave.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                int cant = Integer.parseInt(txtCant.getText());
                Flor florSel = (Flor) cbFlor.getSelectedItem();
                if(florSel == null) throw new Exception("Selecciona flor.");
                Pedido nuevo = new Pedido(id, florSel, cant);
                if(pedido == null) pedidoDAO.insertar(nuevo);
                else pedidoDAO.actualizar(nuevo);
                dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });
    }
}