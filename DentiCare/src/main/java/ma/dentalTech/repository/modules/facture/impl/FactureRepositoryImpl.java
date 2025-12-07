package ma.dentalTech.repository.modules.facture.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.repository.modules.facture.api.FactureRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureRepositoryImpl implements FactureRepository {
    
    @Override
    public Optional<Facture> findById(Long id) {
        String sql = "SELECT * FROM Facture WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la facture", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Facture> findAll() {
        String sql = "SELECT * FROM Facture";
        List<Facture> factures = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                factures.add(mapToFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des factures", e);
        }
        return factures;
    }
    
    @Override
    public Facture save(Facture facture) {
        if (facture.getIdEntite() == null) {
            return insert(facture);
        } else {
            return update(facture);
        }
    }
    
    private Facture insert(Facture facture) {
        String sql = "INSERT INTO Facture (totaleFacture, totalePaye, reste, statut, dateFacture, patient_id, situationFinanciere_id, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, facture.getTotaleFacture());
            stmt.setDouble(2, facture.getTotalePaye());
            stmt.setDouble(3, facture.getReste());
            stmt.setString(4, facture.getStatut().name());
            stmt.setTimestamp(5, Timestamp.valueOf(facture.getDateFacture()));
            stmt.setLong(6, facture.getPatient().getIdEntite());
            stmt.setLong(7, facture.getSituationFinanciere().getIdEntite());
            stmt.setDate(8, Date.valueOf(java.time.LocalDate.now()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                facture.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la facture", e);
        }
        return facture;
    }
    
    private Facture update(Facture facture) {
        String sql = "UPDATE Facture SET totaleFacture = ?, totalePaye = ?, reste = ?, statut = ?, dateFacture = ?, patient_id = ?, situationFinanciere_id = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, facture.getTotaleFacture());
            stmt.setDouble(2, facture.getTotalePaye());
            stmt.setDouble(3, facture.getReste());
            stmt.setString(4, facture.getStatut().name());
            stmt.setTimestamp(5, Timestamp.valueOf(facture.getDateFacture()));
            stmt.setLong(6, facture.getPatient().getIdEntite());
            stmt.setLong(7, facture.getSituationFinanciere().getIdEntite());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(9, facture.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la facture", e);
        }
        return facture;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Facture WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la facture", e);
        }
    }
    
    @Override
    public List<Facture> findByPatientId(Long patientId) {
        String sql = "SELECT * FROM Facture WHERE patient_id = ?";
        List<Facture> factures = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                factures.add(mapToFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des factures", e);
        }
        return factures;
    }
    
    @Override
    public List<Facture> findBySituationFinanciereId(Long situationFinanciereId) {
        String sql = "SELECT * FROM Facture WHERE situationFinanciere_id = ?";
        List<Facture> factures = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, situationFinanciereId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                factures.add(mapToFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des factures", e);
        }
        return factures;
    }
    
    @Override
    public List<Facture> findByStatut(String statut) {
        String sql = "SELECT * FROM Facture WHERE statut = ?";
        List<Facture> factures = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statut);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                factures.add(mapToFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des factures", e);
        }
        return factures;
    }
    
    private Facture mapToFacture(ResultSet rs) throws SQLException {
        return Facture.builder()
                .idEntite(rs.getLong("idEntite"))
                .totaleFacture(rs.getDouble("totaleFacture"))
                .totalePaye(rs.getDouble("totalePaye"))
                .reste(rs.getDouble("reste"))
                .statut(FactureStatutEnum.valueOf(rs.getString("statut")))
                .dateFacture(rs.getTimestamp("dateFacture").toLocalDateTime())
                .build();
    }
}

