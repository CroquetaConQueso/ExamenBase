package ui;

import javax.swing.SwingUtilities;

public class SwingAppMain {

    public static void main(String[] args) {
        UiTheme.install();

        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
        });
    }
}
