package ui;
import model.Distribuidor;
import persistence.DistribuidorDAO;
import javax.swing.*;
import java.awt.*;

public class DistribuidorDialog extends JDialog {
    private DistribuidorDAO dao = new DistribuidorDAO();
    private JTextField txtId = new JTextField();
    private JTextField txtCif = new JTextField();
    private JTextField txtNom = new JTextField();

    public DistribuidorDialog(Window w, Distribuidor d, boolean readOnly) {
        super(w, (readOnly?"Consultar":(d==null?"Nuevo":"Modificar")), ModalityType.APPLICATION_MODAL);
        setSize(400, 300); setLocationRelativeTo(w);
        
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBackground(UiTheme.BG_LIGHT); 
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        UiTheme.styleTextField(txtId); UiTheme.styleTextField(txtCif); UiTheme.styleTextField(txtNom);

        p.add(new JLabel("ID:")); p.add(txtId);
        p.add(new JLabel("CIF:")); p.add(txtCif);
        p.add(new JLabel("Razón Social:")); p.add(txtNom);

        JButton btnSave = UiTheme.createBtn("Guardar");
        if(!readOnly) p.add(new JLabel("")); 
        if(!readOnly) p.add(btnSave);

        add(p);

        if(d != null) {
            txtId.setText(""+d.getIdDistribuidor()); txtId.setEditable(false);
            txtCif.setText(d.getCif());
            txtNom.setText(d.getNombre());
        }
        
        if(readOnly) {
            txtCif.setEditable(false); txtNom.setEditable(false);
        }

        btnSave.addActionListener(e -> {
            try {
                Distribuidor nuevo = new Distribuidor(Integer.parseInt(txtId.getText()), txtCif.getText(), txtNom.getText());
                if(d == null) dao.insertar(nuevo); else dao.actualizar(nuevo);
                dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });
    }
}