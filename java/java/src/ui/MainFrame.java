package ui;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final JTabbedPane tabbedPane;
    private final AbonnementPanel abonnementPanel;
    private final AbonnePanel abonnePanel;
    private final SouscriptionPanel souscriptionPanel;
    private final StatistiquesPanel statistiquesPanel;

    public MainFrame() {
        setTitle("Gestion Salle de Sport");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        abonnementPanel = new AbonnementPanel();
        abonnePanel = new AbonnePanel();
        souscriptionPanel = new SouscriptionPanel();
        statistiquesPanel = new StatistiquesPanel();

        tabbedPane.addTab("Abonnements", new ImageIcon(), abonnementPanel, "Gestion des abonnements");
        tabbedPane.addTab("Abonnés", new ImageIcon(), abonnePanel, "Gestion des abonnés");
        tabbedPane.addTab("Souscriptions", new ImageIcon(), souscriptionPanel, "Gestion des souscriptions");
        tabbedPane.addTab("Statistiques", new ImageIcon(), statistiquesPanel, "Statistiques");

        add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
}