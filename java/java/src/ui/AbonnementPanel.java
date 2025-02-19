
// AbonnementPanel.java
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
        
        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Libellé
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Libellé:"), gbc);
        gbc.gridx = 1;
        libelleField = new JTextField(20);
        formPanel.add(libelleField, gbc);

        // Durée
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Durée (mois):"), gbc);
        gbc.gridx = 1;
        dureeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 36, 1));
        formPanel.add(dureeSpinner, gbc);

        // Prix
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Prix mensuel:"), gbc);
        gbc.gridx = 1;
        prixSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 10.0));
        formPanel.add(prixSpinner, gbc);

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
        String[] columnNames = {"ID", "Libellé", "Durée (mois)", "Prix mensuel"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        ajouterBtn.addActionListener(e -> ajouterAbonnement());
        modifierBtn.addActionListener(e -> modifierAbonnement());
        supprimerBtn.addActionListener(e -> supprimerAbonnement());
        
        // Chargement initial des données
        rafraichirTable();
    }

    private void ajouterAbonnement() {
        String libelle = libelleField.getText().trim();
        if (libelle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le libellé est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int duree = (Integer) dureeSpinner.getValue();
        double prix = (Double) prixSpinner.getValue();

        Abonnement abonnement = new Abonnement(libelle, duree, prix);
        if (abonnementDAO.create(abonnement)) {
            JOptionPane.showMessageDialog(this, "Abonnement ajouté avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierAbonnement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonnement", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        String libelle = libelleField.getText().trim();
        if (libelle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le libellé est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int duree = (Integer) dureeSpinner.getValue();
        double prix = (Double) prixSpinner.getValue();

        Abonnement abonnement = new Abonnement(libelle, duree, prix);
        abonnement.setId(id);

        if (abonnementDAO.update(abonnement)) {
            JOptionPane.showMessageDialog(this, "Abonnement modifié avec succès");
            rafraichirTable();
            reinitialiserFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerAbonnement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonnement", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cet abonnement ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (abonnementDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Abonnement supprimé avec succès");
                rafraichirTable();
                reinitialiserFormulaire();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
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
