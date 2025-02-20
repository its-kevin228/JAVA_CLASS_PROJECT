// AbonnementDAO.java
package dao;

import models.Abonnement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO implements DAO<Abonnement> {
    
    @Override
    public boolean create(Abonnement obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO abonnement (libelle, duree_mois, prix_mensuel) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, obj.getLibelle());
            pstmt.setInt(2, obj.getDureeMois());
            pstmt.setDouble(3, obj.getPrixMensuel());
            
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
    public Abonnement read(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM abonnement WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Abonnement abonnement = new Abonnement(
                    rs.getString("libelle"),
                    rs.getInt("duree_mois"),
                    rs.getDouble("prix_mensuel")
                );
                abonnement.setId(rs.getInt("id"));
                return abonnement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Abonnement obj) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE abonnement SET libelle = ?, duree_mois = ?, prix_mensuel = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            pstmt.setString(1, obj.getLibelle());
            pstmt.setInt(2, obj.getDureeMois());
            pstmt.setDouble(3, obj.getPrixMensuel());
            pstmt.setInt(4, obj.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM abonnement WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM abonnement";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Abonnement abonnement = new Abonnement(
                    rs.getString("libelle"),
                    rs.getInt("duree_mois"),
                    rs.getDouble("prix_mensuel")
                );
                abonnement.setId(rs.getInt("id"));
                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnements;
    }
}
