// StatistiquesPanel.java
package ui;

import dao.AbonneDAO;
import dao.AbonnementDAO;
import dao.SouscriptionDAO;
import models.Abonne;
import models.Abonnement;
import models.Souscription;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class StatistiquesPanel extends JPanel {
    private AbonneDAO abonneDAO;
    private AbonnementDAO abonnementDAO;
    private SouscriptionDAO souscriptionDAO;
    
    private JLabel totalAbonnesLabel;
    private JLabel abonnesActifsLabel;
    private JLabel revenuMensuelLabel;
    private JLabel abonnementPopulaireLabel;

    public StatistiquesPanel() {
        abonneDAO = new AbonneDAO();
        abonnementDAO = new AbonnementDAO();
        souscriptionDAO = new SouscriptionDAO();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Style des statistiques
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);

        // Total des abonnés
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel totalLabel = new JLabel("Total des abonnés:");
        totalLabel.setFont(titleFont);
        add(totalLabel, gbc);

        gbc.gridx = 1;
        totalAbonnesLabel = new JLabel("0");
        totalAbonnesLabel.setFont(valueFont);
        add(totalAbonnesLabel, gbc);

        // Abonnés actifs
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel actifsLabel = new JLabel("Abonnés actifs:");
        actifsLabel.setFont(titleFont);
        add(actifsLabel, gbc);

        gbc.gridx = 1;
        abonnesActifsLabel = new JLabel("0");
        abonnesActifsLabel.setFont(valueFont);
        add(abonnesActifsLabel, gbc);

        // Revenu mensuel
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel revenuLabel = new JLabel("Revenu mensuel:");
        revenuLabel.setFont(titleFont);
        add(revenuLabel, gbc);

        gbc.gridx = 1;
        revenuMensuelLabel = new JLabel("0 €");
        revenuMensuelLabel.setFont(valueFont);
        add(revenuMensuelLabel, gbc);

        // Abonnement le plus populaire
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel populaireLabel = new JLabel("Abonnement le plus populaire:");
        populaireLabel.setFont(titleFont);
        add(populaireLabel, gbc);

        gbc.gridx = 1;
        abonnementPopulaireLabel = new JLabel("Aucun");
        abonnementPopulaireLabel.setFont(valueFont);
        add(abonnementPopulaireLabel, gbc);

        // Bouton de rafraîchissement
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton refreshBtn = new JButton("Rafraîchir les statistiques");
        add(refreshBtn, gbc);

        // Event
        refreshBtn.addActionListener(e -> rafraichirStatistiques());

        // Chargement initial
        rafraichirStatistiques();
    }

    private void rafraichirStatistiques() {
        // Total des abonnés
        List<Abonne> abonnes = abonneDAO.findAll();
        totalAbonnesLabel.setText(String.valueOf(abonnes.size()));

        // Abonnés actifs
        long abonnesActifs = abonnes.stream()
                .filter(Abonne::isStatutSouscription)
                .count();
        abonnesActifsLabel.setText(String.valueOf(abonnesActifs));

        // Calcul du revenu mensuel
        double revenuMensuel = 0;
        List<Souscription> souscriptions = souscriptionDAO.findAll();
        Date currentDate = new Date();

        for (Souscription souscription : souscriptions) {
            Abonnement abonnement = abonnementDAO.read(souscription.getIdAbonnement());
            
            // Vérifie si la souscription est toujours active
            Calendar cal = Calendar.getInstance();
            cal.setTime(souscription.getDateDebut());
            cal.add(Calendar.MONTH, abonnement.getDureeMois());
            Date dateFin = cal.getTime();
            
            if (currentDate.before(dateFin)) {
                revenuMensuel += abonnement.getPrixMensuel();
            }
        }
        revenuMensuelLabel.setText(String.format("%.2f €", revenuMensuel));

        // Abonnement le plus populaire
        if (!souscriptions.isEmpty()) {
            // Compte le nombre de souscriptions par abonnement
            java.util.Map<Integer, Integer> abonnementCount = new java.util.HashMap<>();
            for (Souscription s : souscriptions) {
                abonnementCount.merge(s.getIdAbonnement(), 1, Integer::sum);
            }

            // Trouve l'abonnement le plus populaire
            int idPlusPopulaire = abonnementCount.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(java.util.Map.Entry::getKey)
                    .orElse(0);

            if (idPlusPopulaire > 0) {
                Abonnement plusPopulaire = abonnementDAO.read(idPlusPopulaire);
                abonnementPopulaireLabel.setText(plusPopulaire.getLibelle());
            }
        }
    }
}
