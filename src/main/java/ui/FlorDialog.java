package ui;
import model.Flor;
import persistence.FlorDAO;
import javax.swing.*;
import java.awt.*;

public class FlorDialog extends JDialog {
    private FlorDAO dao = new FlorDAO();
    private JTextField txtId = new JTextField();
    private JTextField txtNom = new JTextField();
    private JTextField txtCol = new JTextField();

    public FlorDialog(Window w, String titulo, Flor flor) {
        super(w, titulo, ModalityType.APPLICATION_MODAL);
        setSize(400, 300); 
        setLocationRelativeTo(w);
        
        // Panel principal con fondo claro
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBackground(UiTheme.BG_LIGHT); 
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Estilar campos
        UiTheme.styleTextField(txtId); 
        UiTheme.styleTextField(txtNom); 
        UiTheme.styleTextField(txtCol);

        // Añadir componentes
        p.add(new JLabel("ID Flor:")); p.add(txtId);
        p.add(new JLabel("Nombre:")); p.add(txtNom);
        p.add(new JLabel("Color:")); p.add(txtCol);

        // Botón corregido (sin pasar colores, usa el estilo por defecto del Theme)
        JButton btnSave = UiTheme.createBtn("Guardar");
        p.add(new JLabel("")); p.add(btnSave);

        add(p);

        // Cargar datos si es modificación
        if(flor != null) {
            txtId.setText(""+flor.getIdFlor()); 
            txtId.setEditable(false);
            txtNom.setText(flor.getNombreFlor());
            txtCol.setText(flor.getColor());
        }

        // Lógica de guardado
        btnSave.addActionListener(e -> {
            try {
                Flor f = new Flor(Integer.parseInt(txtId.getText()), txtNom.getText(), txtCol.getText());
                if(flor == null) dao.insertar(f);
                else dao.actualizar(f);
                dispose();
            } catch(Exception ex) { 
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); 
            }
        });
    }
}