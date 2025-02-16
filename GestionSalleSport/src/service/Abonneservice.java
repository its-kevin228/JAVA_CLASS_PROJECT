/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DBException;
import dao.DatabaseConnection;
import model.Abonne;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ike 
 */
public class Abonneservice {
     // Rechercher un abonné
    public Abonne rechercherAbonneParId(int id) throws DBException {
        String query = "SELECT * FROM abonne WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Abonne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_inscription"),
                        rs.getString("numero_telephone"),
                        rs.getBoolean("statut_souscription")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ajouter un abonné
    public void ajouterAbonne(Abonne abonne) throws DBException {
        // Vérifier si l'abonné existe déjà dans la base
        String checkSql = "SELECT COUNT(*) FROM abonne WHERE numero_telephone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, abonne.getTelephone());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cet abonné existe déjà dans la base de données.");
                return; // Sortir de la méthode si l'abonné existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Encapsuler l'exception SQL dans DBException
        }

        // Insérer l'abonné dans la base de données
        String sql = "INSERT INTO abonne (nom, prenom, numero_telephone, date_inscription, statut_souscription) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, abonne.getNom());
            stmt.setString(2, abonne.getPrenom());
            stmt.setString(3, abonne.getTelephone());
            stmt.setDate(4, new java.sql.Date(abonne.getDateinscription().getTime()));
            stmt.setBoolean(5, abonne.isStatut());
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1); // Récupérer l'ID généré
                    abonne.setId(id); // Mettre à jour l'ID de l'objet Abonne
                    System.out.println("Abonné ajouté avec l'ID : " + id);
                } else {
                    System.out.println("Échec de la récupération de l'ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Encapsuler l'exception SQL dans DBException
        }
    }

    // Modifier un abonné
    public void modifierAbonne(Abonne abonne) throws DBException {
        String sql = "UPDATE abonne SET nom = ?, prenom = ?, numero_telephone = ?, statut_souscription = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonne.getNom());
            stmt.setString(2, abonne.getPrenom());
            stmt.setString(3, abonne.getTelephone());
            stmt.setBoolean(4, abonne.isStatut());
            stmt.setInt(5, abonne.getId());
            stmt.executeUpdate();
            System.out.println("Abonné modifié !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un abonné
    public void supprimerAbonne(int id) throws DBException {
        String sql = "DELETE FROM abonne WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Abonné supprimé !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lister tous les abonnés
    public List<Abonne> getAllAbonnes() throws DBException, SQLException {
        List<Abonne> abonnes = new ArrayList<>();
        String sql = "SELECT * FROM abonne";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                abonnes.add(new Abonne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_inscription"),
                        rs.getString("numero_telephone"),
                        rs.getBoolean("statut_souscription")
                ));
            }
        }
        return abonnes;
    }

    public int getNombreAbonnesActifs() throws DBException {
        String query = "SELECT COUNT(*) FROM abonne WHERE statut_souscription = true";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
