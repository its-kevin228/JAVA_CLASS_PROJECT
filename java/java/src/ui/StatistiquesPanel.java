package ui;

import dao.AbonneDAO;
import dao.AbonnementDAO;
import dao.SouscriptionDAO;
import models.Abonne;
import models.Abonnement;
import models.Souscription;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatistiquesPanel extends JPanel {
    private final AbonneDAO abonneDAO;
    private final AbonnementDAO abonnementDAO;
    private final SouscriptionDAO souscriptionDAO;

    private final JLabel totalAbonnesLabel;
    private final JLabel abonnesActifsLabel;
    private final JLabel revenuMensuelLabel;
    private final JLabel abonnementPopulaireLabel;
    private ChartPanel chartPanel;

    public StatistiquesPanel() {
        abonneDAO = new AbonneDAO();
        abonnementDAO = new AbonnementDAO();
        souscriptionDAO = new SouscriptionDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Statistiques des Abonnements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 102, 204));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setBackground(Color.WHITE);

        totalAbonnesLabel = createStatRow(statsPanel, "Total des abonnés:");
        abonnesActifsLabel = createStatRow(statsPanel, "Abonnés actifs:");
        revenuMensuelLabel = createStatRow(statsPanel, "Revenu mensuel:");
        abonnementPopulaireLabel = createStatRow(statsPanel, "Abonnement le plus populaire:");

        add(statsPanel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Rafraîchir les statistiques");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.setBackground(new Color(0, 102, 204));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshBtn.addActionListener(e -> {
            rafraichirStatistiques();
            updateChart();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        rafraichirStatistiques();
    }

    private JLabel createStatRow(JPanel panel, String title) {
        JLabel labelTitle = new JLabel(title);
        labelTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(labelTitle);

        JLabel labelValue = new JLabel("0");
        labelValue.setFont(new Font("Arial", Font.PLAIN, 16));
        labelValue.setForeground(new Color(51, 51, 51));
        panel.add(labelValue);

        return labelValue;
    }

    private void rafraichirStatistiques() {
        List<Abonne> abonnes = abonneDAO.findAll();
        totalAbonnesLabel.setText(String.valueOf(abonnes.size()));
        long abonnesActifs = abonnes.stream().filter(Abonne::isStatutSouscription).count();
        abonnesActifsLabel.setText(String.valueOf(abonnesActifs));

        double revenuMensuel = 0;
        List<Souscription> souscriptions = souscriptionDAO.findAll();
        Date currentDate = new Date();
        Map<Integer, Integer> abonnementCount = new HashMap<>();

        for (Souscription souscription : souscriptions) {
            Abonnement abonnement = abonnementDAO.read(souscription.getIdAbonnement());
            if (abonnement != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(souscription.getDateDebut());
                cal.add(Calendar.MONTH, abonnement.getDureeMois());
                if (currentDate.before(cal.getTime())) {
                    revenuMensuel += abonnement.getPrixMensuel();
                }
                abonnementCount.put(abonnement.getId(), abonnementCount.getOrDefault(abonnement.getId(), 0) + 1);
            }
        }
        revenuMensuelLabel.setText(String.format("%.2f FCFA", revenuMensuel));

        int idPopulaire = abonnementCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);

        if (idPopulaire != -1) {
            Abonnement plusPopulaire = abonnementDAO.read(idPopulaire);
            abonnementPopulaireLabel.setText(plusPopulaire != null ? plusPopulaire.getLibelle() : "Aucun");
        } else {
            abonnementPopulaireLabel.setText("Aucun");
        }
    }

    private ChartPanel createChartPanel() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Abonnés Actifs", Integer.parseInt(abonnesActifsLabel.getText()));
        dataset.setValue("Total Abonnés", Integer.parseInt(totalAbonnesLabel.getText()));

        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des abonnés",
                dataset,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(new Color(230, 230, 230));
        return new ChartPanel(chart);
    }

    private void updateChart() {
        remove(chartPanel);
        chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}