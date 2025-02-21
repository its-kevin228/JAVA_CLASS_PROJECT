import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Initialiser FlatLaf
        FlatLightLaf.setup();
        // Optionnel : Changer la police par défaut
        UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14));

        SwingUtilities.invokeLater(() -> {
            // Afficher le LoginFrame en premier
            new LoginFrame().setVisible(true);
        });
    }
}