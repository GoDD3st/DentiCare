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
        String sql = "SELECT c.*, dm.id_patient, p.nom as patient_nom, p.telephone as patient_telephone " +
                     "FROM consultation c " +
                     "LEFT JOIN dossier_medical dm ON c.id_dossier_medical = dm.id_dossier " +
                     "LEFT JOIN patient p ON dm.id_patient = p.id_patient " +
                     "ORDER BY c.id_consultation";
        List<Consultation> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) {
                Consultation consultation = mapConsultation(rs);
                // Set dossier medicale and patient data from joined query
                if (rs.getString("patient_nom") != null) {
                    ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier = ma.dentalTech.entities.DossierMedicale.DossierMedicale.builder()
                        .idDossier(rs.getLong("id_dossier_medical"))
                        .build();

                    ma.dentalTech.entities.Patient.Patient patient = ma.dentalTech.entities.Patient.Patient.builder()
                        .idPatient(rs.getLong("id_patient"))
                        .nom(rs.getString("patient_nom"))
                        .telephone(rs.getString("patient_telephone"))
                        .build();

                    dossier.setPatient(patient);
                    consultation.setDossierMedicale(dossier);
                }
                list.add(consultation);
            }
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
        String sql = "INSERT INTO consultation (id_entite, id_dossier_medical, id_medecin, date_consultation, heure_consultation, statut, observationMedecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idDossier = (c.getDossierMedicale() != null) ? c.getDossierMedicale().getIdDossier() : null;
            Long idMedecin = (c.getMedecin() != null) ? c.getMedecin().getIdMedecin() : null;

            if (c.getIdEntite() != null) {
                ps.setLong(1, c.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setLong(2, idDossier);
            if (idMedecin != null) {
                ps.setLong(3, idMedecin);
            } else {
                ps.setNull(3, java.sql.Types.BIGINT);
            }
            ps.setDate(4, c.getDateConsultation() != null ? Date.valueOf(c.getDateConsultation()) : null);
            ps.setTime(5, c.getHeureConsultation() != null ? Time.valueOf(c.getHeureConsultation()) : null);
            ps.setString(6, c.getStatut() != null ? c.getStatut().name() : null);
            ps.setString(7, c.getObservationMedecin());
            ps.setString(8, c.getCreePar());

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
        String sql = "UPDATE consultation SET id_dossier_medical = ?, id_medecin = ?, date_consultation = ?, heure_consultation = ?, statut = ?, observationMedecin = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_consultation = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Long idDossier = (c.getDossierMedicale() != null) ? c.getDossierMedicale().getIdDossier() : null;
            Long idMedecin = (c.getMedecin() != null) ? c.getMedecin().getIdMedecin() : null;

            ps.setLong(1, idDossier);
            ps.setLong(2, idMedecin);
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
