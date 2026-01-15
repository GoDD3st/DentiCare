package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.repository.modules.dossierMedicale.api.InterventionMedecinRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterventionMedecinRepoImpl implements InterventionMedecinRepo {

    @Override
    public List<InterventionMedecin> findAll() throws SQLException {
        String sql = "SELECT * FROM intervention_medecin ORDER BY id_intervention";
        List<InterventionMedecin> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapInterventionMedecin(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<InterventionMedecin> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM intervention_medecin WHERE id_intervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapInterventionMedecin(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(InterventionMedecin im) throws SQLException {
        String sql = "INSERT INTO intervention_medecin (prixDePatient, numDent, id_consultation, id_acte, cree_par) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Extraction des IDs des objets liés
            Long idConsultation = (im.getConsultation() != null) ? im.getConsultation().getIdConsultation() : null;
            Long idActe = (im.getActe() != null) ? im.getActe().getIdActe() : null;

            ps.setObject(1, im.getPrixDePatient());
            ps.setObject(2, im.getNumDent());
            ps.setObject(3, idConsultation);
            ps.setObject(4, idActe);
            ps.setString(5, im.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    im.setIdIntervention(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(InterventionMedecin im) throws SQLException {
        String sql = "UPDATE intervention_medecin SET prixDePatient = ?, numDent = ?, id_consultation = ?, id_acte = ?, modifie_par = ? WHERE id_intervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idConsultation = (im.getConsultation() != null) ? im.getConsultation().getIdConsultation() : null;
            Long idActe = (im.getActe() != null) ? im.getActe().getIdActe() : null;

            ps.setObject(1, im.getPrixDePatient());
            ps.setObject(2, im.getNumDent());
            ps.setObject(3, idConsultation);
            ps.setObject(4, idActe);
            ps.setString(5, im.getModifiePar());
            ps.setLong(6, im.getIdIntervention());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(InterventionMedecin im) throws SQLException {
        if (im != null && im.getIdIntervention() != null) {
            deleteById(im.getIdIntervention());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM intervention_medecin WHERE id_intervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}