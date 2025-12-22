package ma.dentalTech.repository.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class cabinetMedicaleRepoImpl implements cabinetMedicaleRepo {

    @Override
    public List<CabinetMedicale> findAll() throws SQLException {
        String sql = "SELECT * FROM cabinet_medicale ORDER BY id_cabinet";
        List<CabinetMedicale> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapCabinetMedicale(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<CabinetMedicale> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM cabinet_medicale WHERE id_cabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapCabinetMedicale(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(CabinetMedicale cm) throws SQLException {
        String sql = "INSERT INTO cabinet_medicale (nom, email, logo, rue, ville, code_postal, cin, tel1, tel2, site_web, instagram, facebook, description, id_patient, id_situation, id_medecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Extraction des IDs des relations
            Long idPat = (cm.getPatient() != null) ? cm.getPatient().getIdPatient() : null;
            Long idSit = (cm.getSituationFinanciere() != null) ? cm.getSituationFinanciere().getIdSituation() : null;
            Long idMed = (cm.getMedecin() != null) ? cm.getMedecin().getIdMedecin() : null;

            ps.setString(1, cm.getNom());
            ps.setString(2, cm.getEmail());
            ps.setString(3, cm.getLogo());
            // Mapping de l'Adresse
            ps.setString(4, cm.getAdresse() != null ? cm.getAdresse().getRue() : null);
            ps.setString(5, cm.getAdresse() != null ? cm.getAdresse().getVille() : null);
            ps.setString(6, cm.getAdresse() != null ? cm.getAdresse().getCodePostal() : null);

            ps.setString(7, cm.getCin());
            ps.setString(8, cm.getTel1());
            ps.setString(9, cm.getTel2());
            ps.setString(10, cm.getSiteWeb());
            ps.setString(11, cm.getInstagram());
            ps.setString(12, cm.getFacebook());
            ps.setString(13, cm.getDescription());
            ps.setObject(14, idPat);
            ps.setObject(15, idSit);
            ps.setObject(16, idMed);
            ps.setString(17, cm.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cm.setIdCabinet(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(CabinetMedicale cm) throws SQLException {
        String sql = "UPDATE cabinet_medicale SET nom = ?, email = ?, logo = ?, rue = ?, ville = ?, code_postal = ?, cin = ?, tel1 = ?, tel2 = ?, site_web = ?, instagram = ?, facebook = ?, description = ?, id_patient = ?, id_situation = ?, id_medecin = ?, modifie_par = ? WHERE id_cabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idPat = (cm.getPatient() != null) ? cm.getPatient().getIdPatient() : null;
            Long idSit = (cm.getSituationFinanciere() != null) ? cm.getSituationFinanciere().getIdSituation() : null;
            Long idMed = (cm.getMedecin() != null) ? cm.getMedecin().getIdMedecin() : null;

            ps.setString(1, cm.getNom());
            ps.setString(2, cm.getEmail());
            ps.setString(3, cm.getLogo());
            ps.setString(4, cm.getAdresse() != null ? cm.getAdresse().getRue() : null);
            ps.setString(5, cm.getAdresse() != null ? cm.getAdresse().getVille() : null);
            ps.setString(6, cm.getAdresse() != null ? cm.getAdresse().getCodePostal() : null);
            ps.setString(7, cm.getCin());
            ps.setString(8, cm.getTel1());
            ps.setString(9, cm.getTel2());
            ps.setString(10, cm.getSiteWeb());
            ps.setString(11, cm.getInstagram());
            ps.setString(12, cm.getFacebook());
            ps.setString(13, cm.getDescription());
            ps.setObject(14, idPat);
            ps.setObject(15, idSit);
            ps.setObject(16, idMed);
            ps.setString(17, cm.getModifiePar());
            ps.setLong(18, cm.getIdCabinet());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(CabinetMedicale cm) throws SQLException {
        if (cm != null && cm.getIdCabinet() != null) {
            deleteById(cm.getIdCabinet());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM cabinet_medicale WHERE id_cabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}