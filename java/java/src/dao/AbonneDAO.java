package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Abonne;

public class AbonneDAO implements DAO<Abonne> {

    private static final Logger LOGGER = Logger.getLogger(AbonneDAO.class.getName());

    @Override
    public boolean create(Abonne obj) {
        String query = "INSERT INTO abonne (nom, prenom, date_inscription, numero_telephone, statut_souscription) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, obj.getNom());
            pstmt.setString(2, obj.getPrenom());
            pstmt.setDate(3, new java.sql.Date(obj.getDateInscription().getTime()));
            pstmt.setString(4, obj.getNumeroTelephone());
            pstmt.setBoolean(5, obj.isStatutSouscription());

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
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'abonné", e);
            return false;
        }
    }

    @Override
    public Abonne read(int id) {
        String query = "SELECT * FROM abonne WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAbonne(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la lecture de l'abonné", e);
        }
        return null;
    }

    @Override
    public boolean update(Abonne obj) {
        String query = "UPDATE abonne SET nom = ?, prenom = ?, numero_telephone = ?, statut_souscription = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, obj.getNom());
            pstmt.setString(2, obj.getPrenom());
            pstmt.setString(3, obj.getNumeroTelephone());
            pstmt.setBoolean(4, obj.isStatutSouscription());
            pstmt.setInt(5, obj.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'abonné", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM abonne WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'abonné", e);
            return false;
        }
    }

    @Override
    public List<Abonne> findAll() {
        List<Abonne> abonnes = new ArrayList<>();
        String query = "SELECT * FROM abonne";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                abonnes.add(mapResultSetToAbonne(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de la liste des abonnés", e);
        }
        return abonnes;
    }

    public List<Abonne> rechercher(String terme) {
        List<Abonne> abonnes = new ArrayList<>();
        String query = "SELECT * FROM abonne WHERE nom LIKE ? OR prenom LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchTerm = "%" + terme + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    abonnes.add(mapResultSetToAbonne(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'abonnés", e);
        }
        return abonnes;
    }

    private Abonne mapResultSetToAbonne(ResultSet rs) throws SQLException {
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
}