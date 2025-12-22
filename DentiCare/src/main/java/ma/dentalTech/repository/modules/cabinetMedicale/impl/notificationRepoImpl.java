package ma.dentalTech.repository.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.modules.cabinetMedicale.api.notificationRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class notificationRepoImpl implements notificationRepo {

    @Override
    public List<Notification> findAll() throws SQLException {
        String sql = "SELECT t.id_notif, t.titre, t.message, t.date_notif, t.heure_notif, t.type_notif, t.priorite, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM notification t " +
                "JOIN base_entity e ON t.id_notif = e.id_entite " +
                "ORDER BY t.date_notif DESC, t.heure_notif DESC";

        List<Notification> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapNotification(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Notification> findById(Long id) throws SQLException {
        String sql = "SELECT t.id_notif, t.titre, t.message, t.date_notif, t.heure_notif, t.type_notif, t.priorite, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM notification t " +
                "JOIN base_entity e ON t.id_notif = e.id_entite " +
                "WHERE t.id_notif = ?";

        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapNotification(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Notification n) throws SQLException {
        String sql = "INSERT INTO notification (id_notif, titre, message, date_notif, heure_notif, type_notif, priorite) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, n.getIdNotif());
            ps.setString(2, n.getTitre() != null ? n.getTitre().name() : null);
            ps.setString(3, n.getMessage());
            ps.setDate(4, n.getDate() != null ? Date.valueOf(n.getDate()) : null);
            ps.setTime(5, n.getHeure() != null ? Time.valueOf(n.getHeure()) : null);
            ps.setString(6, n.getType() != null ? n.getType().name() : null);
            ps.setString(7, n.getPriorite() != null ? n.getPriorite().name() : null);

            ps.executeUpdate();
        }
    }

    @Override
    public void update(Notification n) throws SQLException {
        String sql = "UPDATE notification SET titre = ?, message = ?, date_notif = ?, heure_notif = ?, type_notif = ?, priorite = ? WHERE id_notif = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, n.getTitre() != null ? n.getTitre().name() : null);
            ps.setString(2, n.getMessage());
            ps.setDate(3, n.getDate() != null ? Date.valueOf(n.getDate()) : null);
            ps.setTime(4, n.getHeure() != null ? Time.valueOf(n.getHeure()) : null);
            ps.setString(5, n.getType() != null ? n.getType().name() : null);
            ps.setString(6, n.getPriorite() != null ? n.getPriorite().name() : null);
            ps.setLong(7, n.getIdNotif());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Notification n) throws SQLException {
        if (n != null && n.getIdNotif() != null) {
            deleteById(n.getIdNotif());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM notification WHERE id_notif = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}