package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.repository.modules.dossierMedicale.api.ConsultationRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.dentalTech.repository.common.RowMappers.mapConsultation;

public class ConsultationRepoImpl implements ConsultationRepo {
    @Override
    public List<Consultation> findAll() throws SQLException{
        String sql = "SELECT * FROM certificat ORDER BY id_consultation";
        List<Consultation> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) list.add(mapConsultation(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Consultation> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM consultation WHERE id_consultation = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapConsultation(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


    @Override
    public void create(Consultation c) throws SQLException {
        String sql = "INSERT INTO consultation (id_dossier_medical, id_medecin, date_consultation, heure_consultation, statut, observationMedecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idDossier = (c.getDossierMedicale() != null) ? c.getDossierMedicale().getIdDossier() : null;
            Long idMedecin = (c.getMedecin() != null) ? c.getMedecin().getIdMedecin() : null;

            ps.setObject(1, idDossier);
            ps.setObject(2, idMedecin);
            ps.setDate(3, c.getDateConsultation() != null ? Date.valueOf(c.getDateConsultation()) : null);
            ps.setTime(4, c.getHeureConsultation() != null ? Time.valueOf(c.getHeureConsultation()) : null);
            ps.setString(5, c.getStatut() != null ? c.getStatut().name() : null);
            ps.setString(6, c.getObservationMedecin());
            ps.setString(7, c.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setIdConsultation(keys.getLong(1));
                }
            }
        }
    }


    @Override
    public void update(Consultation c) throws SQLException {
        String sql = "UPDATE consultation SET id_dossier_medical = ?, id_medecin = ?, date_consultation = ?, heure_consultation = ?, statut = ?, observationMedecin = ?, modifie_par = ? WHERE id_consultation = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Long idDossier = (c.getDossierMedicale() != null) ? c.getDossierMedicale().getIdDossier() : null;
            Long idMedecin = (c.getMedecin() != null) ? c.getMedecin().getIdMedecin() : null;

            ps.setObject(1, idDossier);
            ps.setObject(2, idMedecin);
            ps.setDate(3, c.getDateConsultation() != null ? Date.valueOf(c.getDateConsultation()) : null);
            ps.setTime(4, c.getHeureConsultation() != null ? Time.valueOf(c.getHeureConsultation()) : null);
            ps.setString(5, c.getStatut() != null ? c.getStatut().name() : null);
            ps.setString(6, c.getObservationMedecin());
            ps.setString(7, c.getModifiePar());
            ps.setLong(8, c.getIdConsultation());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Consultation c) throws SQLException {
        if (c != null && c.getIdConsultation() != null) {
            deleteById(c.getIdConsultation());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        String sql = "DELETE FROM consultation WHERE id_consultation = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
