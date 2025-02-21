package dao;

import models.Souscription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SouscriptionDAO implements DAO<Souscription> {

    private static final Logger LOGGER = Logger.getLogger(SouscriptionDAO.class.getName());

    @Override
    public boolean create(Souscription obj) {
        String insertQuery = "INSERT INTO souscription (id_abonne, id_abonnement, date_debut) VALUES (?, ?, ?)";
        String updateAbonneQuery = "UPDATE abonne SET statut_souscription = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setInt(1, obj.getIdAbonne());
                pstmt.setInt(2, obj.getIdAbonnement());
                pstmt.setDate(3, new java.sql.Date(obj.getDateDebut().getTime()));

                if (pstmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateAbonneQuery)) {
                    pstmtUpdate.setInt(1, obj.getIdAbonne());
                    pstmtUpdate.executeUpdate();
                }

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setId(rs.getInt(1));
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la souscription", e);
            return false;
        }
    }

    @Override
    public Souscription read(int id) {
        String query = "SELECT * FROM souscription WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSouscription(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la lecture de la souscription", e);
        }
        return null;
    }

    @Override
    public boolean update(Souscription obj) {
        String query = "UPDATE souscription SET id_abonnement = ?, date_debut = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, obj.getIdAbonnement());
            pstmt.setDate(2, new java.sql.Date(obj.getDateDebut().getTime()));
            pstmt.setInt(3, obj.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la souscription", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String selectQuery = "SELECT id_abonne FROM souscription WHERE id = ?";
        String deleteQuery = "DELETE FROM souscription WHERE id = ?";
        String updateAbonneQuery = "UPDATE abonne SET statut_souscription = false WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtSelect = conn.prepareStatement(selectQuery)) {
                pstmtSelect.setInt(1, id);
                try (ResultSet rs = pstmtSelect.executeQuery()) {
                    if (rs.next()) {
                        int idAbonne = rs.getInt("id_abonne");

                        try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteQuery)) {
                            pstmtDelete.setInt(1, id);
                            pstmtDelete.executeUpdate();
                        }

                        try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateAbonneQuery)) {
                            pstmtUpdate.setInt(1, idAbonne);
                            pstmtUpdate.executeUpdate();
                        }

                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la souscription", e);
            return false;
        }
    }

    @Override
    public List<Souscription> findAll() {
        List<Souscription> souscriptions = new ArrayList<>();
        String query = "SELECT * FROM souscription";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                souscriptions.add(mapResultSetToSouscription(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de la liste des souscriptions", e);
        }
        return souscriptions;
    }

    // Méthode pour trouver la souscription active d'un abonné
    public Souscription findByAbonne(int idAbonne) {
        String query = "SELECT * FROM souscription WHERE id_abonne = ? ORDER BY date_debut DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idAbonne);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSouscription(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la souscription par abonné", e);
        }
        return null;
    }

    private Souscription mapResultSetToSouscription(ResultSet rs) throws SQLException {
        Souscription souscription = new Souscription(
                rs.getInt("id_abonne"),
                rs.getInt("id_abonnement"),
                rs.getDate("date_debut")
        );
        souscription.setId(rs.getInt("id"));
        return souscription;
    }
}