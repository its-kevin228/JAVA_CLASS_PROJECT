// AbonneDAO.java
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Abonne;

public class AbonneDAO implements DAO<Abonne> {

    @Override
    public boolean create(Abonne obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO abonne (nom, prenom, date_inscription,numero_telephone, statut_souscription) VALUES (?, ?,?,?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, obj.getNom());
            pstmt.setString(2, obj.getPrenom());
            pstmt.setDate(3, new java.sql.Date(obj.getDateInscription().getTime()));
            pstmt.setString(4, obj.getNumeroTelephone());
            pstmt.setBoolean(5, obj.isStatutSouscription());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                obj.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Abonne read(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM abonne WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Abonne abonne = new Abonne(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_inscription"),
                    rs.getString("numero_telephone")
                );
                abonne.setId(rs.getInt("id"));
                abonne.setStatutSouscription(rs.getBoolean("statut_souscription"));
                return abonne;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Abonne obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE abonne SET nom = ?, prenom = ?, numero_telephone = ?, statut_souscription = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            pstmt.setString(1, obj.getNom());
            pstmt.setString(2, obj.getPrenom());
            pstmt.setString(3, obj.getNumeroTelephone());
            pstmt.setBoolean(4, obj.isStatutSouscription());
            pstmt.setInt(5, obj.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM abonne WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Abonne> findAll() {
        List<Abonne> abonnes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM abonne";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Abonne abonne = new Abonne(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_inscription"),
                    rs.getString("numero_telephone")
                );
                abonne.setId(rs.getInt("id"));
                abonne.setStatutSouscription(rs.getBoolean("statut_souscription"));
                abonnes.add(abonne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnes;
    }

    // Méthode supplémentaire pour rechercher un abonné par nom ou prénom
    public List<Abonne> rechercher(String terme) {
        List<Abonne> abonnes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM abonne WHERE nom LIKE ? OR prenom LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String searchTerm = "%" + terme + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Abonne abonne = new Abonne(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_inscription"),
                    rs.getString("numero_telephone")
                );
                abonne.setId(rs.getInt("id"));
                abonne.setStatutSouscription(rs.getBoolean("statut_souscription"));
                abonnes.add(abonne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnes;
    }
}
