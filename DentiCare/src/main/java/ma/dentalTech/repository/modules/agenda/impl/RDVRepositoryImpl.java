package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.agenda.api.RDVRepository;
import ma.dentalTech.conf.SessionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RDVRepositoryImpl implements RDVRepository {

    @Override
    public Optional<RDV> findById(Long id) {
        String sql = "SELECT * FROM RDV WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                RDV rdv = RowMappers.mapRDV(rs);
                return Optional.of(rdv);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<RDV> findAll() {
        String sql = "SELECT * FROM RDV";
        List<RDV> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll RDV", e);
        }
        return list;
    }

    @Override
    public void create(RDV rdv) throws SQLException{
        String sql = "INSERT INTO RDV (`date`, heure, motif, statut, noteMedecin, patient_id, medecin_id, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(rdv.getDate()));
            ps.setTime(2, Time.valueOf(rdv.getHeure()));
            ps.setString(3, rdv.getMotif());
            ps.setString(4, rdv.getStatut().name());
            ps.setString(5, rdv.getNoteMedecin());
            ps.setLong(6, rdv.getPatient().getIdEntite());
            ps.setLong(7, rdv.getMedecin().getIdEntite());
            ps.setDate(8, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) rdv.setIdEntite(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insert RDV", e);
        }
        return rdv;
    }

    private RDV update(RDV rdv) {
        String sql = "UPDATE RDV SET `date`=?, heure=?, motif=?, statut=?, noteMedecin=?, patient_id=?, medecin_id=?, dateDerniereModification=? WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(rdv.getDate()));
            stmt.setTime(2, Time.valueOf(rdv.getHeure()));
            stmt.setString(3, rdv.getMotif());
            stmt.setString(4, rdv.getStatut().name());
            stmt.setString(5, rdv.getNoteMedecin());
            stmt.setLong(6, rdv.getPatient().getIdEntite());
            stmt.setLong(7, rdv.getMedecin().getIdEntite());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(9, rdv.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update RDV", e);
        }
        return rdv;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM RDV WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete RDV", e);
        }
    }

    @Override
    public List<RDV> findByPatientId(Long patientId) {
        return findByColumn("patient_id", patientId);
    }

    @Override
    public List<RDV> findByMedecinId(Long medecinId) {
        return findByColumn("medecin_id", medecinId);
    }

    // Helper générique pour éviter la duplication de code
    private List<RDV> findByColumn(String column, Object value) {
        String sql = "SELECT * FROM RDV WHERE " + column + " = ?";
        List<RDV> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche RDV par " + column, e);
        }
        return list;
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        String sql = "SELECT * FROM RDV WHERE `date` = ?";
        List<RDV> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche RDV par date", e);
        }
        return list;
    }

    @Override
    public List<RDV> findByStatut(String statut) {
        return findByColumn("statut", statut);
    }

}