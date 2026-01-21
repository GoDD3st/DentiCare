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
        String sql = "SELECT * FROM cabinet_medical ORDER BY id_cabinet";
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
        String sql = "SELECT * FROM cabinet_medical WHERE id_cabinet = ?";
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
        String sql = "INSERT INTO cabinet_medical (nom, email, logo, adresse, tel1, tel2, siteWeb, instagram, facebook, description, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cm.getNom());
            ps.setString(2, cm.getEmail());
            ps.setString(3, cm.getLogo());

            // Mapping de l'Adresse - créer une adresse formatée
            String adresseStr = null;
            if (cm.getAdresse() != null) {
                StringBuilder adresseBuilder = new StringBuilder();
                if (cm.getAdresse().getRue() != null && !cm.getAdresse().getRue().trim().isEmpty()) {
                    adresseBuilder.append(cm.getAdresse().getRue().trim());
                }
                if (cm.getAdresse().getVille() != null && !cm.getAdresse().getVille().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getVille().trim());
                }
                if (cm.getAdresse().getCodePostal() != null && !cm.getAdresse().getCodePostal().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(" ");
                    adresseBuilder.append(cm.getAdresse().getCodePostal().trim());
                }
                if (cm.getAdresse().getRégion() != null && !cm.getAdresse().getRégion().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getRégion().trim());
                }
                if (cm.getAdresse().getPays() != null && !cm.getAdresse().getPays().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getPays().trim());
                }
                adresseStr = adresseBuilder.toString();
            }
            ps.setString(4, adresseStr);

            ps.setString(5, cm.getTel1());
            ps.setString(6, cm.getTel2());
            ps.setString(7, cm.getSiteWeb());
            ps.setString(8, cm.getInstagram());
            ps.setString(9, cm.getFacebook());
            ps.setString(10, cm.getDescription());
            ps.setString(11, cm.getCreePar());

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
        String sql = "UPDATE cabinet_medical SET nom = ?, email = ?, logo = ?, adresse = ?, tel1 = ?, tel2 = ?, siteWeb = ?, instagram = ?, facebook = ?, description = ?, modifie_par = ? WHERE id_cabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cm.getNom());
            ps.setString(2, cm.getEmail());
            ps.setString(3, cm.getLogo());

            // Mapping de l'Adresse - créer une adresse formatée
            String adresseStr = null;
            if (cm.getAdresse() != null) {
                StringBuilder adresseBuilder = new StringBuilder();
                if (cm.getAdresse().getRue() != null && !cm.getAdresse().getRue().trim().isEmpty()) {
                    adresseBuilder.append(cm.getAdresse().getRue().trim());
                }
                if (cm.getAdresse().getVille() != null && !cm.getAdresse().getVille().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getVille().trim());
                }
                if (cm.getAdresse().getCodePostal() != null && !cm.getAdresse().getCodePostal().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(" ");
                    adresseBuilder.append(cm.getAdresse().getCodePostal().trim());
                }
                if (cm.getAdresse().getRégion() != null && !cm.getAdresse().getRégion().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getRégion().trim());
                }
                if (cm.getAdresse().getPays() != null && !cm.getAdresse().getPays().trim().isEmpty()) {
                    if (adresseBuilder.length() > 0) adresseBuilder.append(", ");
                    adresseBuilder.append(cm.getAdresse().getPays().trim());
                }
                adresseStr = adresseBuilder.toString();
            }
            ps.setString(4, adresseStr);

            ps.setString(5, cm.getTel1());
            ps.setString(6, cm.getTel2());
            ps.setString(7, cm.getSiteWeb());
            ps.setString(8, cm.getInstagram());
            ps.setString(9, cm.getFacebook());
            ps.setString(10, cm.getDescription());
            ps.setString(11, cm.getModifiePar());
            ps.setLong(12, cm.getIdCabinet());

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
        String sql = "DELETE FROM cabinet_medical WHERE id_cabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}