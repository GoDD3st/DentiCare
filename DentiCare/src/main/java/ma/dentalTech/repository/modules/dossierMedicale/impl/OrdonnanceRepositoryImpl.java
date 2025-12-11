package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.dentalTech.repository.common.RowMappers.mapOrdonnance;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepo {
    
    @Override
    public Optional<Ordonnance> findById(Long id) {
        String sql = "SELECT * FROM Ordonnance WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapOrdonnance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'ordonnance", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Ordonnance> findAll() {
        String sql = "SELECT * FROM Ordonnance";
        List<Ordonnance> ordonnances = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ordonnances.add(mapOrdonnance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ordonnances", e);
        }
        return ordonnances;
    }
    
    @Override
    public Ordonnance save(Ordonnance ordonnance) {
        if (ordonnance.getIdEntite() == null) {
            return insert(ordonnance);
        } else {
            return update(ordonnance);
        }
    }
    
    private Ordonnance insert(Ordonnance ordonnance) {
        String sql = "INSERT INTO Ordonnance (`date`, consultation_id, dateCreation) VALUES (?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(ordonnance.getDate()));
            Consultation consultation = ordonnance.getConsultations() != null && !ordonnance.getConsultations().isEmpty() 
                    ? ordonnance.getConsultations().get(0) : null;
            if (consultation == null || consultation.getIdEntite() == null) {
                throw new RuntimeException("Consultation is required for ordonnance");
            }

            stmt.setLong(2, consultation.getIdEntite());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ordonnance.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'ordonnance", e);
        }
        return ordonnance;
    }
    
    private Ordonnance update(Ordonnance ordonnance) {
        String sql = "UPDATE Ordonnance SET `date` = ?, consultation_id = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(ordonnance.getDate()));
            Consultation consultation = ordonnance.getConsultations() != null && !ordonnance.getConsultations().isEmpty() 
                    ? ordonnance.getConsultations().get(0) : null;
            if (consultation == null || consultation.getIdEntite() == null) {
                throw new RuntimeException("Consultation is required for ordonnance");
            }
            stmt.setLong(2, consultation.getIdEntite());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(4, ordonnance.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'ordonnance", e);
        }
        return ordonnance;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Ordonnance WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'ordonnance", e);
        }
    }
}

