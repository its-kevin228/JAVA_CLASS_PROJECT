// SouscriptionPanel.java
package ui;

import dao.AbonneDAO;
import dao.AbonnementDAO;
import dao.SouscriptionDAO;
import models.Abonne;
import models.Abonnement;
import models.Souscription;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sélection de l'abonné
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Abonné:"), gbc);
        gbc.gridx = 1;
        abonneCombo = new JComboBox<>();
        formPanel.add(abonneCombo, gbc);

        // Sélection de l'abonnement
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Abonnement:"), gbc);
        gbc.gridx = 1;
        abonnementCombo = new JComboBox<>();
        formPanel.add(abonnementCombo, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton souscrireBtn = new JButton("Souscrire");
        JButton supprimerBtn = new JButton("Supprimer");
        buttonPanel.add(souscrireBtn);
        buttonPanel.add(supprimerBtn);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] columnNames = {"ID", "Abonné", "Abonnement", "Date début", "Date fin"};
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
        souscrireBtn.addActionListener(e -> souscrire());
        supprimerBtn.addActionListener(e -> supprimerSouscription());

        // Chargement initial des données
        rafraichirComboBox();
        rafraichirTable();
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
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un abonné et un abonnement",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Souscription souscription = new Souscription(abonne.getId(), abonnement.getId(), new Date());

        if (souscriptionDAO.create(souscription)) {
            JOptionPane.showMessageDialog(this, "Souscription effectuée avec succès");
            rafraichirTable();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la souscription",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerSouscription() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une souscription",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette souscription ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (souscriptionDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Souscription supprimée avec succès");
                rafraichirTable();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        List<Souscription> souscriptions = souscriptionDAO.findAll();
        for (Souscription souscription : souscriptions) {
            Abonne abonne = abonneDAO.read(souscription.getIdAbonne());
            Abonnement abonnement = abonnementDAO.read(souscription.getIdAbonnement());
            
            // Calcul de la date de fin
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(souscription.getDateDebut());
            cal.add(java.util.Calendar.MONTH, abonnement.getDureeMois());
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
