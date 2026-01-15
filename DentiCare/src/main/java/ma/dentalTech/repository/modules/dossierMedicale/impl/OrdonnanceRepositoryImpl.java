package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepo {

    @Override
    public List<Ordonnance> findAll() throws SQLException {
        String sql = "SELECT o.*, dm.id_patient, p.nom as patient_nom, p.telephone as patient_telephone " +
                     "FROM ordonnance o " +
                     "LEFT JOIN dossier_medical dm ON o.id_dossier_medical = dm.id_dossier " +
                     "LEFT JOIN patient p ON dm.id_patient = p.id_patient " +
                     "ORDER BY o.id_ordonnance";
        List<Ordonnance> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ordonnance ordonnance = RowMappers.mapOrdonnance(rs);
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
                    ordonnance.setDossierMedicale(dossier);
                }
                list.add(ordonnance);
            }
        }
        return list;
    }

    @Override
    public Optional<Ordonnance> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapOrdonnance(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Ordonnance o) throws SQLException {
        String sql = "INSERT INTO ordonnance (id_entite, date_ord, note, id_dossier_medical, id_consultation, cree_par) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (o.getIdEntite() != null) {
                ps.setLong(1, o.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setDate(2, o.getDate() != null ? Date.valueOf(o.getDate()) : null);
            ps.setString(3, o.getNote());

            // Handle both single and list relationships
            Long idDossier = null;
            if (o.getDossierMedicale() != null) {
                idDossier = o.getDossierMedicale().getIdDossier();
            } else if (o.getDossiersMedicales() != null && !o.getDossiersMedicales().isEmpty()) {
                idDossier = o.getDossiersMedicales().get(0).getIdDossier();
            }

            Long idConsultation = null;
            if (o.getConsultation() != null) {
                idConsultation = o.getConsultation().getIdConsultation();
            } else if (o.getConsultations() != null && !o.getConsultations().isEmpty()) {
                idConsultation = o.getConsultations().get(0).getIdConsultation();
            }

            if (idDossier != null) {
                ps.setLong(4, idDossier);
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }

            if (idConsultation != null) {
                ps.setLong(5, idConsultation);
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            ps.setString(6, o.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    o.setIdOrdonnance(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Ordonnance o) throws SQLException {
        String sql = "UPDATE ordonnance SET date_ord = ?, note = ?, id_dossier_medical = ?, id_consultation = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, o.getDate() != null ? Date.valueOf(o.getDate()) : null);
            ps.setString(2, o.getNote());
            ps.setLong(3, o.getDossierMedicale() != null ? o.getDossierMedicale().getIdDossier() : null);
            ps.setLong(4, o.getConsultation() != null ? o.getConsultation().getIdConsultation() : null);
            ps.setString(5, o.getModifiePar());
            ps.setLong(6, o.getIdOrdonnance());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Ordonnance o) throws SQLException {
        if (o != null && o.getIdOrdonnance() != null) {
            deleteById(o.getIdOrdonnance());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM ordonnance WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}