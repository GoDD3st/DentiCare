package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.modules.agenda.api.RDVRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RDVRepoImpl implements RDVRepository {

    @Override
    public List<RDV> findAll() throws SQLException {
        String sql = "SELECT * FROM rdv ORDER BY id_rdv";
        List<RDV> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapRDV(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<RDV> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM rdv WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapRDV(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(RDV r) throws SQLException {
        String sql = "INSERT INTO rdv (date_rdv, heure_rdv, motif, statut, note_medecin, id_dossier_medical, id_consultation, id_patient, id_medecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idDossier = (r.getDossierMedicale() != null) ? r.getDossierMedicale().getIdDossier() : null;
            Long idCons = (r.getConsultation() != null) ? r.getConsultation().getIdConsultation() : null;
            Long idPat = (r.getPatient() != null) ? r.getPatient().getIdPatient() : null;
            Long idMed = (r.getMedecin() != null) ? r.getMedecin().getIdMedecin() : null;

            ps.setDate(1, r.getDate() != null ? Date.valueOf(r.getDate()) : null);
            ps.setTime(2, r.getHeure() != null ? Time.valueOf(r.getHeure()) : null);
            ps.setString(3, r.getMotif());
            ps.setString(4, r.getStatut() != null ? r.getStatut().name() : null);
            ps.setString(5, r.getNoteMedecin());
            ps.setObject(6, idDossier);
            ps.setObject(7, idCons);
            ps.setObject(8, idPat);
            ps.setObject(9, idMed);
            ps.setString(10, r.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setIdRDV(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(RDV r) throws SQLException {
        String sql = "UPDATE rdv SET date_rdv = ?, heure_rdv = ?, motif = ?, statut = ?, note_medecin = ?, id_dossier_medical = ?, id_consultation = ?, id_patient = ?, id_medecin = ?, modifie_par = ? WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idDossier = (r.getDossierMedicale() != null) ? r.getDossierMedicale().getIdDossier() : null;
            Long idCons = (r.getConsultation() != null) ? r.getConsultation().getIdConsultation() : null;
            Long idPat = (r.getPatient() != null) ? r.getPatient().getIdPatient() : null;
            Long idMed = (r.getMedecin() != null) ? r.getMedecin().getIdMedecin() : null;

            ps.setDate(1, r.getDate() != null ? Date.valueOf(r.getDate()) : null);
            ps.setTime(2, r.getHeure() != null ? Time.valueOf(r.getHeure()) : null);
            ps.setString(3, r.getMotif());
            ps.setString(4, r.getStatut() != null ? r.getStatut().name() : null);
            ps.setString(5, r.getNoteMedecin());
            ps.setObject(6, idDossier);
            ps.setObject(7, idCons);
            ps.setObject(8, idPat);
            ps.setObject(9, idMed);
            ps.setString(10, r.getModifiePar());
            ps.setLong(11, r.getIdRDV());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(RDV r) throws SQLException {
        if (r != null && r.getIdRDV() != null) {
            deleteById(r.getIdRDV());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM rdv WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}