
// AbonnePanel.java
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
        
        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        rechercheField = new JTextField(20);
        searchPanel.add(rechercheField);
        JButton rechercherBtn = new JButton("Rechercher");
        searchPanel.add(rechercherBtn);
        
        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        nomField = new JTextField(20);
        formPanel.add(nomField, gbc);

        // Prénom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        prenomField = new JTextField(20);
        formPanel.add(prenomField, gbc);

        // Téléphone
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        telephoneField = new JTextField(20);
        formPanel.add(telephoneField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ajouterBtn = new JButton("Ajouter");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        buttonPanel.add(ajouterBtn);
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Date Inscription", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        ajouterBtn.addActionListener(e -> ajouterAbonne());
        modifierBtn.addActionListener(e -> modifierAbonne());
        supprimerBtn.addActionListener(e -> supprimerAbonne());
        rechercherBtn.addActionListener(e -> rechercherAbonnes());
        
        // Sélection dans la table
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    nomField.setText((String) table.getValueAt(selectedRow, 1));
                    prenomField.setText((String) table.getValueAt(selectedRow, 2));
                    telephoneField.setText((String) table.getValueAt(selectedRow, 3));
                }
            }
        });
        
        // Chargement initial des données
        rafraichirTable();
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
            JOptionPane.showMessageDialog(this, "Abonné ajouté avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierAbonne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validerChamps()) return;

        int id = (Integer) table.getValueAt(selectedRow, 0);
        Abonne abonne = abonneDAO.read(id);
        abonne.setNom(nomField.getText().trim());
        abonne.setPrenom(prenomField.getText().trim());
        abonne.setNumeroTelephone(telephoneField.getText().trim());

        if (abonneDAO.update(abonne)) {
            JOptionPane.showMessageDialog(this, "Abonné modifié avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerAbonne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cet abonné ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (abonneDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Abonné supprimé avec succès");
                rafraichirTable();
                reinitialiserFormulaire();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rechercherAbonnes() {
        String terme = rechercheField.getText().trim();
        tableModel.setRowCount(0);
        List<Abonne> abonnes;
        
        if (terme.isEmpty()) {
            abonnes = abonneDAO.findAll();
        } else {
            abonnes = abonneDAO.rechercher(terme);
        }
        
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
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (prenomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le prénom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (telephoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro de téléphone est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
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
