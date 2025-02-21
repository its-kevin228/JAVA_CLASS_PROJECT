package ui;

import dao.AbonneDAO;
import dao.AbonnementDAO;
import dao.SouscriptionDAO;
import models.Abonne;
import models.Abonnement;
import models.Souscription;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SouscriptionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private SouscriptionDAO souscriptionDAO;
    private AbonneDAO abonneDAO;
    private AbonnementDAO abonnementDAO;
    private JComboBox<Abonne> abonneCombo;
    private JComboBox<Abonnement> abonnementCombo;

    public SouscriptionPanel() {
        souscriptionDAO = new SouscriptionDAO();
        abonneDAO = new AbonneDAO();
        abonnementDAO = new AbonnementDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel formPanel = initFormPanel();
        JScrollPane scrollPane = new JScrollPane(initTable());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        rafraichirComboBox();
        rafraichirTable();
    }

    private JPanel initFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Style personnalisé pour les labels
        JLabel abonneLabel = createStyledLabel("Abonné:");
        JLabel abonnementLabel = createStyledLabel("Abonnement:");

        // Abonné
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(abonneLabel, gbc);
        gbc.gridx = 1;
        abonneCombo = createStyledComboBox();
        formPanel.add(abonneCombo, gbc);

        // Abonnement
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(abonnementLabel, gbc);
        gbc.gridx = 1;
        abonnementCombo = createStyledComboBox();
        formPanel.add(abonnementCombo, gbc);

        // Panel des boutons avec nouveau style
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton souscrireBtn = createStyledButton("Souscrire", new Color(46, 204, 113));
        JButton supprimerBtn = createStyledButton("Supprimer", new Color(231, 76, 60));
        JButton rafraichirBtn = createStyledButton("Rafraîchir", new Color(52, 152, 219));
        JButton renouvelerBtn = createStyledButton("Renouveler", new Color(155, 89, 182));

        buttonPanel.add(souscrireBtn);
        buttonPanel.add(supprimerBtn);
        buttonPanel.add(renouvelerBtn);
        buttonPanel.add(rafraichirBtn);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Ajout des actions avec effets d'animation
        souscrireBtn.addActionListener(e -> {
            animateButton(souscrireBtn);
            souscrire();
        });

        supprimerBtn.addActionListener(e -> {
            animateButton(supprimerBtn);
            supprimerSouscription();
        });

        rafraichirBtn.addActionListener(e -> {
            animateButton(rafraichirBtn);
            rafraichirComboBox();  // Ajout de cette ligne
            rafraichirTable();
        });

        renouvelerBtn.addActionListener(e -> {
            animateButton(renouvelerBtn);
            renouvelerSouscription();
        });

        return formPanel;
    }

    private JTable initTable() {
        String[] columnNames = {"ID", "Abonné", "Abonnement", "Date début", "Date fin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        // Style de la table
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(189, 195, 199));

        // Style de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        return table;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JComboBox createStyledComboBox() {
        JComboBox comboBox = new JComboBox();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Arial", Font.BOLD, 15));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Arial", Font.BOLD, 14));
            }
        });

        return button;
    }

    private void animateButton(JButton button) {
        Timer timer = new Timer(50, null);
        final int[] frame = {0};

        timer.addActionListener(e -> {
            if (frame[0] < 5) {
                button.setSize(button.getWidth() - 2, button.getHeight() - 2);
                frame[0]++;
            } else if (frame[0] < 10) {
                button.setSize(button.getWidth() + 2, button.getHeight() + 2);
                frame[0]++;
            } else {
                timer.stop();
            }
            button.revalidate();
        });

        timer.start();
    }

    private void rafraichirComboBox() {
        abonneCombo.removeAllItems();
        abonnementCombo.removeAllItems();

        List<Abonne> abonnes = abonneDAO.findAll();
        for (Abonne abonne : abonnes) {
            abonneCombo.addItem(abonne);
        }

        List<Abonnement> abonnements = abonnementDAO.findAll();
        for (Abonnement abonnement : abonnements) {
            abonnementCombo.addItem(abonnement);
        }
    }

    private void souscrire() {
        Abonne abonne = (Abonne) abonneCombo.getSelectedItem();
        Abonnement abonnement = (Abonnement) abonnementCombo.getSelectedItem();

        if (abonne == null || abonnement == null) {
            showStyledMessage("Veuillez sélectionner un abonné et un abonnement", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Souscription souscription = new Souscription(abonne.getId(), abonnement.getId(), new Date());
        if (souscriptionDAO.create(souscription)) {
            showStyledMessage("Souscription effectuée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rafraichirTable();
        } else {
            showStyledMessage("Erreur lors de la souscription", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerSouscription() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Veuillez sélectionner une souscription", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (Integer) table.getValueAt(selectedRow, 0);

        UIManager.put("OptionPane.yesButtonText", "Oui");
        UIManager.put("OptionPane.noButtonText", "Non");

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette souscription ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (souscriptionDAO.delete(id)) {
                showStyledMessage("Souscription supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            } else {
                showStyledMessage("Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void renouvelerSouscription() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Veuillez sélectionner une souscription à renouveler", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idSouscription = (Integer) table.getValueAt(selectedRow, 0);
        Souscription ancienneSouscription = souscriptionDAO.read(idSouscription);

        if (ancienneSouscription != null) {
            Souscription nouvelleSouscription = new Souscription(
                    ancienneSouscription.getIdAbonne(),
                    ancienneSouscription.getIdAbonnement(),
                    new Date()
            );

            if (souscriptionDAO.create(nouvelleSouscription)) {
                showStyledMessage("Abonnement renouvelé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                rafraichirTable();
            } else {
                showStyledMessage("Erreur lors du renouvellement de l'abonnement", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            showStyledMessage("Erreur lors de la lecture de la souscription", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStyledMessage(String message, String titre, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        JOptionPane.showMessageDialog(this, message, titre, messageType);
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Souscription> souscriptions = souscriptionDAO.findAll();
        for (Souscription souscription : souscriptions) {
            Abonne abonne = abonneDAO.read(souscription.getIdAbonne());
            Abonnement abonnement = abonnementDAO.read(souscription.getIdAbonnement());
            Calendar cal = Calendar.getInstance();
            cal.setTime(souscription.getDateDebut());
            cal.add(Calendar.MONTH, abonnement.getDureeMois());
            Date dateFin = cal.getTime();
            Object[] row = {
                    souscription.getId(),
                    abonne.getNom() + " " + abonne.getPrenom(),
                    abonnement.getLibelle(),
                    souscription.getDateDebut(),
                    dateFin
            };
            tableModel.addRow(row);
        }
    }
}