package ui;

import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginFrame() {
        setTitle("Login");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        messageLabel = new JLabel("");

        usernameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(150, 200, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(70, 130, 180));
            }
        });

        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(messageLabel, gbc);

        JPanel imagePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\Ameble\\Desktop\\java\\java\\images\\logo.jpg");
                Image img = icon.getImage();
                int imgHeight = getHeight();
                int imgWidth = (img.getWidth(null) * imgHeight) / img.getHeight(null);
                g.drawImage(img, 0, 0, imgWidth, imgHeight, null);
            }
        };
        imagePanel.setPreferredSize(new Dimension(200, getHeight()));
        imagePanel.setBackground(new Color(240, 240, 240));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagePanel, loginPanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.5);
        add(splitPane);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        System.out.println("Tentative de connexion avec : " + username + " / " + password);

        if ("PAK".equals(username) && "PAK".equals(password)) {
            System.out.println("Connexion validée, ouverture du MainFrame...");
            messageLabel.setText("Connexion réussie");
            messageLabel.setForeground(new Color(34, 139, 34));
            dispose(); // Fermer la fenêtre de login
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            });
        } else {
            System.out.println("Échec de la connexion");
            messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
            messageLabel.setForeground(Color.RED);
        }
    }
}