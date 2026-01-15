package ma.dentalTech.repository.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Log.Log;
import ma.dentalTech.repository.modules.cabinetMedicale.api.logRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class logRepoImpl implements logRepo {

    @Override
    public List<Log> findAll() throws SQLException {
        String sql = "SELECT t.id_log, t.action, t.date_heure, t.ip_adresse, t.status, t.id_utilisateur, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM log t " +
                "JOIN base_entity e ON t.id_log = e.id_entite " +
                "ORDER BY t.date_heure DESC";
        List<Log> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapLog(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Log> findById(Long id) throws SQLException {
        String sql = "SELECT t.id_log, t.action, t.date_heure, t.ip_adresse, t.status, t.id_utilisateur, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM log t " +
                "JOIN base_entity e ON t.id_log = e.id_entite " +
                "WHERE t.id_log = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapLog(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Log l) throws SQLException {
        String sql = "INSERT INTO log (id_log, action, date_heure, ip_adresse, status, id_utilisateur) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idUser = (l.getUtilisateur() != null) ? l.getUtilisateur().getIdUser() : null;

            ps.setString(1, l.getAction());
            ps.setTimestamp(2, l.getDateHeure() != null ? Timestamp.valueOf(l.getDateHeure()) : null);
            ps.setString(3, l.getIpAdresse());
            ps.setString(4, l.getStatus());
            ps.setObject(5, idUser);
            ps.setString(6, l.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    l.setIdLog(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Log l) throws SQLException {
        // En général, un Log ne se modifie pas pour des raisons d'intégrité d'audit.
        // Mais voici l'implémentation si nécessaire :
        String sql = "UPDATE log SET action = ?, status = ? WHERE id_log = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getAction());
            ps.setString(2, l.getStatus());
            ps.setString(3, l.getModifiePar());
            ps.setLong(4, l.getIdLog());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Log l) throws SQLException {
        if (l != null && l.getIdLog() != null) {
            deleteById(l.getIdLog());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM log WHERE id_log = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}