package ma.dentalTech.repository.modules.patient.impl;

import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {

    @Override
    public Optional<Patient> findById(Long id) throws InterruptedException {
        String sql = "SELECT * FROM Patients WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapPatient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du patient", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Patient> findAll() {
        String sql = "SELECT * FROM Patients";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des patients", e);
        }
        return list;
    }

    @Override
    public Patient save(Patient patient) {
        if (patient.getIdEntite() == null) return insert(patient);
        else return update(patient);
    }

    private Patient insert(Patient p) {
        String sql = "INSERT INTO Patients (nom, dateDeNaissance, sexe, adresse, telephone, assurance, dossierMedicale_id, situationFinanciere_id, dateCreation) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNom());
            if (p.getDateNaissance() != null) stmt.setDate(2, Date.valueOf(p.getDateNaissance())); else stmt.setNull(2, Types.DATE);
            stmt.setString(3, p.getSexe() != null ? p.getSexe().name() : null);
            stmt.setString(4, p.getAdresse());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getAssurance() != null ? p.getAssurance().name() : null);
            if (p.getDossierMedicale() != null && p.getDossierMedicale().getIdEntite() != null) {
                stmt.setLong(7, p.getDossierMedicale().getIdEntite());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            // situationFinanciere_id not present on entity; set null
            stmt.setNull(8, Types.BIGINT);
            stmt.setDate(9, Date.valueOf(LocalDate.now()));

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) p.setIdEntite(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du patient", e);
        }
        return p;
    }

    private Patient update(Patient p) {
        String sql = "UPDATE Patients SET nom = ?, dateDeNaissance = ?, sexe = ?, adresse = ?, telephone = ?, assurance = ?, dossierMedicale_id = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNom());
            if (p.getDateNaissance() != null) stmt.setDate(2, Date.valueOf(p.getDateNaissance())); else stmt.setNull(2, Types.DATE);
            stmt.setString(3, p.getSexe() != null ? p.getSexe().name() : null);
            stmt.setString(4, p.getAdresse());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getAssurance() != null ? p.getAssurance().name() : null);
            if (p.getDossierMedicale() != null && p.getDossierMedicale().getIdEntite() != null) {
                stmt.setLong(7, p.getDossierMedicale().getIdEntite());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            stmt.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(9, p.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du patient", e);
        }
        return p;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Patients WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du patient", e);
        }
    }

    @Override
    public List<Patient> findByNom(String nom) {
        String sql = "SELECT * FROM Patients WHERE nom LIKE ?";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(RowMappers.mapPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des patients par nom", e);
        }
        return list;
    }

    @Override
    public List<Patient> findByAssurance(String assurance) {
        String sql = "SELECT * FROM Patients WHERE assurance = ?";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assurance);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(RowMappers.mapPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des patients par assurance", e);
        }
        return list;
    }

    @Override
    public Optional<Patient> findByDossierMedicaleId(Long dossierId) {
        String sql = "SELECT * FROM Patients WHERE dossierMedicale_id = ? LIMIT 1";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, dossierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(RowMappers.mapPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du patient par dossier médical", e);
        }
        return Optional.empty();
    }
}

