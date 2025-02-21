package ui;

import dao.AbonneDAO;
import models.Abonne;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AbonnePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private AbonneDAO abonneDAO;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField telephoneField;
    private JTextField rechercheField;

    public AbonnePanel() {
        abonneDAO = new AbonneDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel topPanel = initTopPanel();
        JScrollPane scrollPane = new JScrollPane(initTable());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                nomField.setText((String) table.getValueAt(table.getSelectedRow(), 1));
                prenomField.setText((String) table.getValueAt(table.getSelectedRow(), 2));
                telephoneField.setText((String) table.getValueAt(table.getSelectedRow(), 3));
            }
        });

        rafraichirTable();
    }

    private JPanel initTopPanel() {
        // Panel de recherche avec style moderne
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 240, 245));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(searchLabel);

        rechercheField = new JTextField(20);
        rechercheField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 255), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.add(rechercheField);

        JButton rechercherBtn = createStyledButton("Rechercher", new Color(100, 100, 255));
        searchPanel.add(rechercherBtn);
        rechercherBtn.addActionListener(e -> rechercherAbonnes());

        JButton rafraichirBtn = createStyledButton("Rafraîchir", new Color(142, 68, 173));
        searchPanel.add(rafraichirBtn);
        rafraichirBtn.addActionListener(e -> rafraichirTable());

        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Style des labels
        Font labelFont = new Font("Arial", Font.BOLD, 13);

        // Champs de saisie avec style
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setFont(labelFont);
        formPanel.add(nomLabel, gbc);
        gbc.gridx = 1;
        nomField = createStyledTextField();
        formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel prenomLabel = new JLabel("Prénom:");
        prenomLabel.setFont(labelFont);
        formPanel.add(prenomLabel, gbc);
        gbc.gridx = 1;
        prenomField = createStyledTextField();
        formPanel.add(prenomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel telLabel = new JLabel("Téléphone:");
        telLabel.setFont(labelFont);
        formPanel.add(telLabel, gbc);
        gbc.gridx = 1;
        telephoneField = createStyledTextField();
        formPanel.add(telephoneField, gbc);

        // Panel des boutons avec effet de survol
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton ajouterBtn = createStyledButton("Ajouter", new Color(46, 204, 113));
        JButton modifierBtn = createStyledButton("Modifier", new Color(52, 152, 219));
        JButton supprimerBtn = createStyledButton("Supprimer", new Color(231, 76, 60));

        buttonPanel.add(ajouterBtn);
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        ajouterBtn.addActionListener(e -> ajouterAbonne());
        modifierBtn.addActionListener(e -> modifierAbonne());
        supprimerBtn.addActionListener(e -> supprimerAbonne());

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        return topPanel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JTable initTable() {
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Date Inscription", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        // Style de la table
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(150, 131, 236));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(232, 241, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));

        return table;
    }

    private void ajouterAbonne() {
        if (!validerChamps()) return;
        Abonne abonne = new Abonne(
                nomField.getText().trim(),
                prenomField.getText().trim(),
                new Date(),
                telephoneField.getText().trim()
        );
        if (abonneDAO.create(abonne)) {
            showSuccessMessage("Abonné ajouté avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            showErrorMessage("Erreur lors de l'ajout");
        }
    }

    private void modifierAbonne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || !validerChamps()) {
            showErrorMessage("Veuillez sélectionner un abonné et remplir correctement les champs");
            return;
        }
        int id = (Integer) table.getValueAt(selectedRow, 0);
        Abonne abonne = abonneDAO.read(id);
        abonne.setNom(nomField.getText().trim());
        abonne.setPrenom(prenomField.getText().trim());
        abonne.setNumeroTelephone(telephoneField.getText().trim());
        if (abonneDAO.update(abonne)) {
            showSuccessMessage("Abonné modifié avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            showErrorMessage("Erreur lors de la modification");
        }
    }

    private void supprimerAbonne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Veuillez sélectionner un abonné");
            return;
        }
        int id = (Integer) table.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cet abonné ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (abonneDAO.delete(id)) {
                showSuccessMessage("Abonné supprimé avec succès");
                rafraichirTable();
                reinitialiserFormulaire();
            } else {
                showErrorMessage("Erreur lors de la suppression");
            }
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void rechercherAbonnes() {
        String terme = rechercheField.getText().trim();
        tableModel.setRowCount(0);
        List<Abonne> abonnes = terme.isEmpty() ? abonneDAO.findAll() : abonneDAO.rechercher(terme);
        for (Abonne abonne : abonnes) {
            Object[] row = {
                    abonne.getId(),
                    abonne.getNom(),
                    abonne.getPrenom(),
                    abonne.getNumeroTelephone(),
                    abonne.getDateInscription(),
                    abonne.isStatutSouscription() ? "Actif" : "Inactif"
            };
            tableModel.addRow(row);
        }
    }

    private boolean validerChamps() {
        if (nomField.getText().trim().isEmpty()) {
            showErrorMessage("Le nom est obligatoire");
            return false;
        }
        if (prenomField.getText().trim().isEmpty()) {
            showErrorMessage("Le prénom est obligatoire");
            return false;
        }
        if (telephoneField.getText().trim().isEmpty()) {
            showErrorMessage("Le numéro de téléphone est obligatoire");
            return false;
        }
        return true;
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Abonne> abonnes = abonneDAO.findAll();
        for (Abonne abonne : abonnes) {
            Object[] row = {
                    abonne.getId(),
                    abonne.getNom(),
                    abonne.getPrenom(),
                    abonne.getNumeroTelephone(),
                    abonne.getDateInscription(),
                    abonne.isStatutSouscription() ? "Actif" : "Inactif"
            };
            tableModel.addRow(row);
        }
    }

    private void reinitialiserFormulaire() {
        nomField.setText("");
        prenomField.setText("");
        telephoneField.setText("");
        table.clearSelection();
    }
}