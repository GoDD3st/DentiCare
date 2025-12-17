package ma.dentalTech.repository.modules.patient.impl;

import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {


    public List<Patient> findAll() throws SQLException{
        String sql = "SELECT * FROM patients ORDER BY id";
        List<Patient> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(RowMappers.mapPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Patient> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM patient WHERE idEntite = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapPatient(rs));
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Patient p) throws SQLException {
        String sql = "INSERT INTO (nom, sexe, adresse, telephone, dateNaissance, assurance, date_creation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,p.getNom());
            ps.setString(2,p.getSexe().name());
            ps.setString(3,p.getAdresse());
            ps.setString(4,p.getTelephone());
            ps.setObject(5, p.getDateNaissance());
            ps.setString(6,p.getAssurance().name());
            ps.setObject(7, LocalDateTime.now());  // dateCreaation

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setIdPatient(keys.getLong(1));}
            }
        } catch (SQLException e)
        { throw new RuntimeException(e); }
    }

    @Override
    public void update(Patient p) throws SQLException{
        String sql = "UPDATE patient SET nom = ?, sexe = ?, adresse = ?, telephone = ?, dateNaissance = ?," +
                " assurance = ?, date_derniere_modification = ?  WHERE id_patient = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getSexe().name());
            ps.setString(3, p.getAdresse());
            ps.setString(4, p.getTelephone());
            ps.setObject(5, p.getDateNaissance());
            ps.setString(6, p.getAssurance().name());
            ps.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setLong(8, p.getIdPatient());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(Patient p) throws SQLException{
        if (p != null) deleteById(p.getIdPatient());
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        String sql = "DELETE FROM patient WHERE id_patient = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
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
    } */




    /* @Override
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
    */
    }



