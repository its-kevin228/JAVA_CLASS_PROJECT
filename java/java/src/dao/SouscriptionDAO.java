// SouscriptionDAO.java
package dao;

import models.Souscription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SouscriptionDAO implements DAO<Souscription> {
    
    @Override
    public boolean create(Souscription obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Début de la transaction
            conn.setAutoCommit(false);
            
            try {
                // Création de la souscription
                String query = "INSERT INTO souscription (id_abonne, id_abonnement, date_debut) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                
                pstmt.setInt(1, obj.getIdAbonne());
                pstmt.setInt(2, obj.getIdAbonnement());
                pstmt.setDate(3, new java.sql.Date(obj.getDateDebut().getTime()));
                
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }
                
                // Mise à jour du statut de l'abonné
                String updateAbonne = "UPDATE abonne SET statut_souscription = true WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateAbonne);
                pstmtUpdate.setInt(1, obj.getIdAbonne());
                pstmtUpdate.executeUpdate();
                
                // Validation de la transaction
                conn.commit();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Souscription read(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM souscription WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Souscription souscription = new Souscription(
                    rs.getInt("id_abonne"),
                    rs.getInt("id_abonnement"),
                    rs.getDate("date_debut")
                );
                souscription.setId(rs.getInt("id"));
                return souscription;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Souscription obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE souscription SET id_abonnement = ?, date_debut = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            pstmt.setInt(1, obj.getIdAbonnement());
            pstmt.setDate(2, new java.sql.Date(obj.getDateDebut().getTime()));
            pstmt.setInt(3, obj.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Récupérer l'ID de l'abonné avant la suppression
                String querySelect = "SELECT id_abonne FROM souscription WHERE id = ?";
                PreparedStatement pstmtSelect = conn.prepareStatement(querySelect);
                pstmtSelect.setInt(1, id);
                ResultSet rs = pstmtSelect.executeQuery();
                
                if (rs.next()) {
                    int idAbonne = rs.getInt("id_abonne");
                    
                    // Supprimer la souscription
                    String queryDelete = "DELETE FROM souscription WHERE id = ?";
                    PreparedStatement pstmtDelete = conn.prepareStatement(queryDelete);
                    pstmtDelete.setInt(1, id);
                    pstmtDelete.executeUpdate();
                    
                    // Mettre à jour le statut de l'abonné
                    String updateAbonne = "UPDATE abonne SET statut_souscription = false WHERE id = ?";
                    PreparedStatement pstmtUpdate = conn.prepareStatement(updateAbonne);
                    pstmtUpdate.setInt(1, idAbonne);
                    pstmtUpdate.executeUpdate();
                    
                    conn.commit();
                    return true;
                }
                
                conn.rollback();
                return false;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Souscription> findAll() {
        List<Souscription> souscriptions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM souscription";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Souscription souscription = new Souscription(
                    rs.getInt("id_abonne"),
                    rs.getInt("id_abonnement"),
                    rs.getDate("date_debut")
                );
                souscription.setId(rs.getInt("id"));
                souscriptions.add(souscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return souscriptions;
    }

    // Méthode pour trouver la souscription active d'un abonné
    public Souscription findByAbonne(int idAbonne) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM souscription WHERE id_abonne = ? ORDER BY date_debut DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idAbonne);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Souscription souscription = new Souscription(
                    rs.getInt("id_abonne"),
                    rs.getInt("id_abonnement"),
                    rs.getDate("date_debut")
                );
                souscription.setId(rs.getInt("id"));
                return souscription;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
