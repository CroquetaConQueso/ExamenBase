package ui;
import model.Flor;
import persistence.FlorDAO;
import javax.swing.*;
import java.awt.*;

public class FlorDialog extends JDialog {
    private FlorDAO dao = new FlorDAO();
    private JTextField txtId = new JTextField(10);
    private JTextField txtNom = new JTextField(15);
    private JTextField txtCol = new JTextField(15);

    public FlorDialog(Window w, String titulo, Flor flor) {
        super(w, titulo, ModalityType.APPLICATION_MODAL);
        setSize(400, 300); setLocationRelativeTo(w);
        
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBackground(UiTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        UiTheme.styleTextField(txtId); UiTheme.styleTextField(txtNom); UiTheme.styleTextField(txtCol);

        p.add(new JLabel("ID Flor:")); p.add(txtId);
        p.add(new JLabel("Nombre:")); p.add(txtNom);
        p.add(new JLabel("Color:")); p.add(txtCol);

        JButton btnSave = UiTheme.createBtn("Guardar", UiTheme.ACCENT, Color.BLACK);
        p.add(new JLabel("")); p.add(btnSave);

        add(p);

        if(flor != null) {
            txtId.setText(""+flor.getIdFlor()); txtId.setEditable(false);
            txtNom.setText(flor.getNombreFlor());
            txtCol.setText(flor.getColor());
        }

        btnSave.addActionListener(e -> {
            try {
                Flor f = new Flor(Integer.parseInt(txtId.getText()), txtNom.getText(), txtCol.getText());
                if(flor == null) dao.insertar(f);
                else dao.actualizar(f);
                dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });
    }
}