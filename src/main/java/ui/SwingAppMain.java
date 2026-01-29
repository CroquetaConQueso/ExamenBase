package ui;
import javax.swing.SwingUtilities;

public class SwingAppMain {
    public static void main(String[] args) {
        UiTheme.install();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}