package ma.dentalTech.repository.modules.prescription.impl;

import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.repository.modules.prescription.api.PrescriptionRepository;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {
    
    @Override
    public Optional<Prescription> findById(Long id) {
        String sql = "SELECT * FROM Prescription WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToPrescription(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la prescription", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Prescription> findAll() {
        String sql = "SELECT * FROM Prescription";
        List<Prescription> prescriptions = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prescriptions.add(mapToPrescription(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des prescriptions", e);
        }
        return prescriptions;
    }
    
    @Override
    public Prescription save(Prescription prescription) {
        if (prescription.getIdEntite() == null) {
            return insert(prescription);
        } else {
            return update(prescription);
        }
    }
    
    private Prescription insert(Prescription prescription) {
        String sql = "INSERT INTO Prescription (quantite, frequence, dureeEnJours, ordonnance_id, medicament_id, dateCreation) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prescription.getQuantite());
            stmt.setString(2, prescription.getFrequence());
            stmt.setInt(3, prescription.getDureeEnJours());
            stmt.setLong(4, prescription.getOrdonnance().getIdEntite());
            stmt.setLong(5, prescription.getMedicament().getIdEntite());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                prescription.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la prescription", e);
        }
        return prescription;
    }
    
    private Prescription update(Prescription prescription) {
        String sql = "UPDATE Prescription SET quantite = ?, frequence = ?, dureeEnJours = ?, ordonnance_id = ?, medicament_id = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescription.getQuantite());
            stmt.setString(2, prescription.getFrequence());
            stmt.setInt(3, prescription.getDureeEnJours());
            stmt.setLong(4, prescription.getOrdonnance().getIdEntite());
            stmt.setLong(5, prescription.getMedicament().getIdEntite());
            stmt.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(7, prescription.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la prescription", e);
        }
        return prescription;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Prescription WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la prescription", e);
        }
    }
    
    @Override
    public List<Prescription> findByOrdonnanceId(Long ordonnanceId) {
        String sql = "SELECT * FROM Prescription WHERE ordonnance_id = ?";
        List<Prescription> prescriptions = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ordonnanceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                prescriptions.add(mapToPrescription(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des prescriptions", e);
        }
        return prescriptions;
    }
    
    @Override
    public List<Prescription> findByMedicamentId(Long medicamentId) {
        String sql = "SELECT * FROM Prescription WHERE medicament_id = ?";
        List<Prescription> prescriptions = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, medicamentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                prescriptions.add(mapToPrescription(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des prescriptions", e);
        }
        return prescriptions;
    }
    
    private Prescription mapToPrescription(ResultSet rs) throws SQLException {
        Prescription p = Prescription.builder()
                .quantite(rs.getInt("quantite"))
                .frequence(rs.getString("frequence"))
                .dureeEnJours(rs.getInt("dureeEnJours"))
                .build();
        p.setIdEntite(rs.getLong("idEntite"));
        return p;
    }
}

