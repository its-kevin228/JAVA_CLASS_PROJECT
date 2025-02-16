/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DBException;
import dao.DatabaseConnection;
import model.Abonnement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ike
 */
public class Abonnementservice {
     // Ajouter un abonnement
    public void ajouterAbonnement(Abonnement abonnement) {
        String query = "INSERT INTO abonnement (libelle, duree_mois, prix_mensuel) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, abonnement.getLibelle());
            stmt.setInt(2, abonnement.getDureeMois());
            stmt.setDouble(3, abonnement.getPrixMensuel());
            stmt.executeUpdate();
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
    }

    // Modifier un abonnement
    public void modifierAbonnement(Abonnement abonnement) {
        String sql = "UPDATE abonnement SET libelle = ?, duree_mois = ?, prix_mensuel = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonnement.getLibelle());
            stmt.setInt(2, abonnement.getDureeMois());
            stmt.setDouble(3, abonnement.getPrixMensuel());
            stmt.setInt(4, abonnement.getId());
            stmt.executeUpdate();
            System.out.println("Abonnement modifié !");
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un abonnement
    public void supprimerAbonnement(int id) {
        String sql = "DELETE FROM abonnement WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Abonnement supprimé !");
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
    }

    // Lister tous les abonnements
    public List<Abonnement> getAllAbonnements() {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                abonnements.add(new Abonnement(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("duree_mois"),
                        rs.getDouble("prix_mensuel")
                ));
            }
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
        return abonnements;
    }

    public double calculerChiffreAffairesMensuel() throws DBException {
        String query = "SELECT SUM(a.prix_mensuel) FROM abonnement a " +
                "JOIN souscription s ON a.id = s.id_abonnement";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
}
