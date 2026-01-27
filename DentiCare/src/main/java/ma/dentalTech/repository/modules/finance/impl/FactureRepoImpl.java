package ma.dentalTech.repository.modules.finance.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.finance.api.FactureRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureRepoImpl implements FactureRepo {

    @Override
    public List<Facture> findAll() throws SQLException {
        String sql =
                "SELECT f.*, " +
                "c.id_dossier_medical, dm.id_patient, p.nom AS patient_nom, p.telephone AS patient_telephone " +
                "FROM facture f " +
                "LEFT JOIN consultation c ON f.id_consultation = c.id_consultation " +
                "LEFT JOIN dossier_medical dm ON c.id_dossier_medical = dm.id_dossier " +
                "LEFT JOIN patient p ON dm.id_patient = p.id_patient " +
                "ORDER BY f.id_facture";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Facture f = RowMappers.mapFacture(rs);

                if (rs.getObject("id_dossier_medical") != null && rs.getObject("id_patient") != null) {
                    ma.dentalTech.entities.Patient.Patient patient = ma.dentalTech.entities.Patient.Patient.builder()
                            .idPatient(rs.getLong("id_patient"))
                            .nom(rs.getString("patient_nom"))
                            .telephone(rs.getString("patient_telephone"))
                            .build();

                    ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier =
                            ma.dentalTech.entities.DossierMedicale.DossierMedicale.builder()
                                    .idDossier(rs.getLong("id_dossier_medical"))
                                    .patient(patient)
                                    .build();

                    ma.dentalTech.entities.Consultation.Consultation consultation =
                            ma.dentalTech.entities.Consultation.Consultation.builder()
                                    .idConsultation(rs.getLong("id_consultation"))
                                    .dossierMedicale(dossier)
                                    .build();

                    f.setConsultation(consultation);
                }

                list.add(f);
            }
        }
        return list;
    }

    @Override
    public Optional<Facture> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM facture WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapFacture(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Facture f) throws SQLException {
        String sql = "INSERT INTO facture (id_entite, totale_facture, totale_paye, statut, date_facture, id_situation_financiere, id_consultation, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idSit = (f.getSituationFinanciere() != null) ? f.getSituationFinanciere().getIdSituation() : null;
            Long idCons = (f.getConsultation() != null) ? f.getConsultation().getIdConsultation() : null;

            if (f.getIdEntite() != null) {
                ps.setLong(1, f.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setObject(2, f.getTotaleFacture());
            ps.setObject(3, f.getTotalePaye());
            ps.setString(4, f.getStatut() != null ? f.getStatut().name() : null);
            ps.setTimestamp(5, f.getDateFacture() != null ? Timestamp.valueOf(f.getDateFacture()) : null);
            ps.setObject(6, idSit);
            ps.setObject(7, idCons);
            ps.setString(8, f.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    f.setIdFacture(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Facture f) throws SQLException {
        String sql = "UPDATE facture SET totale_facture = ?, totale_paye = ?, statut = ?, date_facture = ?, id_situation_financiere = ?, id_consultation = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idSit = (f.getSituationFinanciere() != null) ? f.getSituationFinanciere().getIdSituation() : null;
            Long idCons = (f.getConsultation() != null) ? f.getConsultation().getIdConsultation() : null;

            ps.setObject(1, f.getTotaleFacture());
            ps.setObject(2, f.getTotalePaye());
            ps.setString(3, f.getStatut() != null ? f.getStatut().name() : null);
            ps.setTimestamp(4, f.getDateFacture() != null ? Timestamp.valueOf(f.getDateFacture()) : null);
            ps.setObject(5, idSit);
            ps.setObject(6, idCons);
            ps.setString(7, f.getModifiePar());
            ps.setLong(8, f.getIdFacture());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Facture f) throws SQLException {
        if (f != null && f.getIdFacture() != null) {
            deleteById(f.getIdFacture());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM facture WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}