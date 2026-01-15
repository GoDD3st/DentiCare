package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.repository.modules.dossierMedicale.api.DossierMedicaleRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DossierMedicalRepoImpl implements DossierMedicaleRepo {

    @Override
    public List<DossierMedicale> findAll() throws SQLException {
        String sql = "SELECT dm.*, p.nom as patient_nom, p.telephone as patient_telephone " +
                     "FROM dossier_medical dm " +
                     "LEFT JOIN patient p ON dm.id_patient = p.id_patient " +
                     "ORDER BY dm.id_dossier";
        List<DossierMedicale> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DossierMedicale dossier = RowMappers.mapDossierMedicale(rs);
                // Set patient data from joined query
                if (rs.getString("patient_nom") != null) {
                    Patient patient = Patient.builder()
                        .idPatient(rs.getLong("id_patient"))
                        .nom(rs.getString("patient_nom"))
                        .telephone(rs.getString("patient_telephone"))
                        .build();
                    dossier.setPatient(patient);
                }
                list.add(dossier);
            }
        }
        return list;
    }

    @Override
    public Optional<DossierMedicale> findById(Long id) throws SQLException {
        String sql = "SELECT dm.*, p.nom as patient_nom, p.telephone as patient_telephone " +
                     "FROM dossier_medical dm " +
                     "LEFT JOIN patient p ON dm.id_patient = p.id_patient " +
                     "WHERE dm.id_dossier = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DossierMedicale dossier = RowMappers.mapDossierMedicale(rs);
                    // Set patient data from joined query
                    if (rs.getString("patient_nom") != null) {
                        Patient patient = Patient.builder()
                            .idPatient(rs.getLong("id_patient"))
                            .nom(rs.getString("patient_nom"))
                            .telephone(rs.getString("patient_telephone"))
                            .build();
                        dossier.setPatient(patient);
                    }
                    return Optional.of(dossier);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(DossierMedicale dm) throws SQLException {
        String sql = "INSERT INTO dossier_medical (id_entite, id_patient, cree_par) VALUES (?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (dm.getIdEntite() != null) {
                ps.setLong(1, dm.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            Long idPatient = (dm.getPatient() != null) ? dm.getPatient().getIdPatient() : null;

            ps.setLong(2, idPatient);
            ps.setString(3, dm.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    dm.setIdDossier(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(DossierMedicale dm) throws SQLException {
        String sql = "UPDATE dossier_medical SET id_patient = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_dossier = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idPatient = (dm.getPatient() != null) ? dm.getPatient().getIdPatient() : null;

            ps.setLong(1, idPatient);
            ps.setString(2, dm.getModifiePar());
            ps.setLong(3, dm.getIdDossier());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(DossierMedicale dm) throws SQLException {
        if (dm != null && dm.getIdDossier() != null) {
            deleteById(dm.getIdDossier());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM dossier_medical WHERE id_dossier = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}