package dao;

import models.Abonnement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbonnementDAO implements DAO<Abonnement> {

    private static final Logger LOGGER = Logger.getLogger(AbonnementDAO.class.getName());

    @Override
    public boolean create(Abonnement obj) {
        String query = "INSERT INTO abonnement (libelle, duree_mois, prix_mensuel) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, obj.getLibelle());
            pstmt.setInt(2, obj.getDureeMois());
            pstmt.setDouble(3, obj.getPrixMensuel());

            if (pstmt.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'abonnement", e);
            return false;
        }
    }

    @Override
    public Abonnement read(int id) {
        String query = "SELECT * FROM abonnement WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAbonnement(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la lecture de l'abonnement", e);
        }
        return null;
    }

    @Override
    public boolean update(Abonnement obj) {
        String query = "UPDATE abonnement SET libelle = ?, duree_mois = ?, prix_mensuel = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, obj.getLibelle());
            pstmt.setInt(2, obj.getDureeMois());
            pstmt.setDouble(3, obj.getPrixMensuel());
            pstmt.setInt(4, obj.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'abonnement", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM abonnement WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'abonnement", e);
            return false;
        }
    }

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                abonnements.add(mapResultSetToAbonnement(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de la liste des abonnements", e);
        }
        return abonnements;
    }

    private Abonnement mapResultSetToAbonnement(ResultSet rs) throws SQLException {
        Abonnement abonnement = new Abonnement(
                rs.getString("libelle"),
                rs.getInt("duree_mois"),
                rs.getDouble("prix_mensuel")
        );
        abonnement.setId(rs.getInt("id"));
        return abonnement;
    }
}