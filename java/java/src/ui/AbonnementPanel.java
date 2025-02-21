package ui;

import dao.AbonnementDAO;
import models.Abonnement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AbonnementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private AbonnementDAO abonnementDAO;
    private JTextField libelleField;
    private JSpinner dureeSpinner;
    private JSpinner prixSpinner;

    public AbonnementPanel() {
        abonnementDAO = new AbonnementDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));

        JPanel formPanel = initFormPanel();
        JScrollPane scrollPane = new JScrollPane(initTable());

        // Style de la table
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        rafraichirTable();
    }

    private JPanel initFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Abonnements");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Champs de formulaire avec style
        JPanel inputPanel = createStyledInputPanel();
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(inputPanel, gbc);

        // Boutons avec style moderne
        JPanel buttonPanel = createStyledButtonPanel();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private JPanel createStyledInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Style des labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = new Color(44, 62, 80);

        // Libellé
        JLabel libelleLabel = new JLabel("Libellé:");
        libelleLabel.setFont(labelFont);
        libelleLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(libelleLabel, gbc);

        libelleField = new JTextField(20);
        styleTextField(libelleField);
        gbc.gridx = 1;
        inputPanel.add(libelleField, gbc);

        // Durée
        JLabel dureeLabel = new JLabel("Durée (mois):");
        dureeLabel.setFont(labelFont);
        dureeLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(dureeLabel, gbc);

        dureeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 36, 1));
        styleSpinner(dureeSpinner);
        gbc.gridx = 1;
        inputPanel.add(dureeSpinner, gbc);

        // Prix
        JLabel prixLabel = new JLabel("Prix mensuel:");
        prixLabel.setFont(labelFont);
        prixLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(prixLabel, gbc);

        prixSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 10.0));
        styleSpinner(prixSpinner);
        gbc.gridx = 1;
        inputPanel.add(prixSpinner, gbc);

        return inputPanel;
    }

    private JPanel createStyledButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        JButton ajouterBtn = createStyledButton("Ajouter", new Color(46, 204, 113));
        JButton modifierBtn = createStyledButton("Modifier", new Color(52, 152, 219));
        JButton supprimerBtn = createStyledButton("Supprimer", new Color(231, 76, 60));

        buttonPanel.add(ajouterBtn);
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);

        // Events avec effets d'animation
        ajouterBtn.addActionListener(e -> {
            animateButton(ajouterBtn);
            ajouterAbonnement();
        });
        modifierBtn.addActionListener(e -> {
            animateButton(modifierBtn);
            modifierAbonnement();
        });
        supprimerBtn.addActionListener(e -> {
            animateButton(supprimerBtn);
            supprimerAbonnement();
        });

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(Color.WHITE);
    }

    private void animateButton(JButton button) {
        Color originalColor = button.getBackground();
        button.setBackground(originalColor.brighter());
        Timer timer = new Timer(100, e -> button.setBackground(originalColor));
        timer.setRepeats(false);
        timer.start();
    }

    private JTable initTable() {
        String[] columnNames = {"ID", "Libellé", "Durée (mois)", "Prix mensuel"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        // Ajout d'un listener pour la sélection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    libelleField.setText(table.getValueAt(selectedRow, 1).toString());
                    dureeSpinner.setValue(Integer.parseInt(table.getValueAt(selectedRow, 2).toString()));
                    prixSpinner.setValue(Double.parseDouble(table.getValueAt(selectedRow, 3).toString()));
                }
            }
        });

        return table;
    }

    private void ajouterAbonnement() {
        String libelle = libelleField.getText().trim();
        if (libelle.isEmpty()) {
            afficherMessageErreur("Le libellé est obligatoire");
            return;
        }
        int duree = (Integer) dureeSpinner.getValue();
        double prix = (Double) prixSpinner.getValue();

        Abonnement abonnement = new Abonnement(libelle, duree, prix);
        if (abonnementDAO.create(abonnement)) {
            afficherMessageSucces("Abonnement ajouté avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            afficherMessageErreur("Erreur lors de l'ajout");
        }
    }

    private void modifierAbonnement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            afficherMessageErreur("Veuillez sélectionner un abonnement");
            return;
        }
        String libelle = libelleField.getText().trim();
        if (libelle.isEmpty()) {
            afficherMessageErreur("Le libellé est obligatoire");
            return;
        }
        int duree = (Integer) dureeSpinner.getValue();
        double prix = (Double) prixSpinner.getValue();
        int id = (Integer) table.getValueAt(selectedRow, 0);

        Abonnement abonnement = new Abonnement(libelle, duree, prix);
        abonnement.setId(id);

        if (abonnementDAO.update(abonnement)) {
            afficherMessageSucces("Abonnement modifié avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            afficherMessageErreur("Erreur lors de la modification");
        }
    }

    private void supprimerAbonnement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            afficherMessageErreur("Veuillez sélectionner un abonnement");
            return;
        }
        int id = (Integer) table.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cet abonnement ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (abonnementDAO.delete(id)) {
                afficherMessageSucces("Abonnement supprimé avec succès");
                rafraichirTable();
                reinitialiserFormulaire();
            } else {
                afficherMessageErreur("Erreur lors de la suppression");
            }
        }
    }

    private void afficherMessageSucces(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void afficherMessageErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Abonnement> abonnements = abonnementDAO.findAll();
        for (Abonnement abonnement : abonnements) {
            Object[] row = {
                    abonnement.getId(),
                    abonnement.getLibelle(),
                    abonnement.getDureeMois(),
                    abonnement.getPrixMensuel()
            };
            tableModel.addRow(row);
        }
    }

    private void reinitialiserFormulaire() {
        libelleField.setText("");
        dureeSpinner.setValue(1);
        prixSpinner.setValue(0.0);
        table.clearSelection();
    }
}