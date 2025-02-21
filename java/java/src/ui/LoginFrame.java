package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("FitnessPro - Connexion");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal avec deux sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Section gauche avec dégradé et image
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dégradé de fond plus moderne
                GradientPaint gp = new GradientPaint(
                        0, 0, Color.WHITE,  // Changé de la couleur bleue au blanc
                        getWidth(), getHeight(), Color.BLACK  // Changé de la couleur rose au noir
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Ajout d'un effet d'overlay
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Configuration du layout pour le panneau gauche
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 0, 20);

        // Ajout de l'image
        try {
            //ImageIcon originalIcon = new ImageIcon(getClass().getResource("C:\\Users\\Ameble\\Desktop\\java\\java\\images\\Gym Inspiration.jpeg"));
            ImageIcon originalIcon = new ImageIcon("C:\\Users\\Ameble\\Desktop\\java\\java\\images\\home gym - Merhan Elhalawany.jpeg");
            Image img = originalIcon.getImage();

            // Redimensionnement de l'image
            int newWidth = 200;
            int newHeight = (int) ((double) newWidth / img.getWidth(null) * img.getHeight(null));
            Image resizedImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(resizedImg));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(imageLabel, gbc);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image : " + e.getMessage());
        }

        // Titre principal avec style moderne
        JLabel titleLabel = new JLabel("FITNESS PRO");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.insets = new Insets(20, 20, 10, 20);
        leftPanel.add(titleLabel, gbc);

        // Sous-titre
        JLabel subtitleLabel = new JLabel("VENEZ VOUS MUSCLEZ AVEC NOUS");
        subtitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.insets = new Insets(0, 20, 20, 20);
        leftPanel.add(subtitleLabel, gbc);

        // Ajout d'un panneau décoratif
        JPanel decorPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), 3, 3, 3);
            }
        };
        decorPanel.setOpaque(false);
        decorPanel.setPreferredSize(new Dimension(100, 3));
        gbc.insets = new Insets(0, 20, 40, 20);
        leftPanel.add(decorPanel, gbc);

        // Section droite avec le formulaire
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridwidth = GridBagConstraints.REMAINDER;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(10, 40, 10, 40);

        // Titre de connexion
        JLabel loginTitle = new JLabel("Connexion");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(loginTitle, gbcRight);

        // Champ username
        usernameField = new JTextField(20);
        styleTextField(usernameField, "Nom d'utilisateur");
        rightPanel.add(Box.createVerticalStrut(30), gbcRight);
        rightPanel.add(usernameField, gbcRight);

        // Champ password
        passwordField = new JPasswordField(20);
        styleTextField(passwordField, "Mot de passe");
        rightPanel.add(Box.createVerticalStrut(15), gbcRight);
        rightPanel.add(passwordField, gbcRight);

        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");
        styleButton(loginButton);
        rightPanel.add(Box.createVerticalStrut(30), gbcRight);
        rightPanel.add(loginButton, gbcRight);

        // Ajout des panels au panel principal
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        setContentPane(mainPanel);

        // Action du bouton de connexion
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                openMainFrame();
                dispose();
            } else {
                showErrorMessage();
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        return username.equals("PAK") && password.equals("PAK");
    }

    private void openMainFrame() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private void showErrorMessage() {
        JOptionPane.showMessageDialog(this,
                "Nom d'utilisateur ou mot de passe incorrect",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE);
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(200, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        if (field instanceof JTextField && !(field instanceof JPasswordField)) {
            field.setText(placeholder);
            field.setForeground(Color.GRAY);

            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(63, 94, 251));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(252, 70, 107));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(63, 94, 251));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}