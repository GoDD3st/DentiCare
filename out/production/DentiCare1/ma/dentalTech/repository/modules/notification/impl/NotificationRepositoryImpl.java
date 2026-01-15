package ma.dentalTech.repository.modules.notification.impl;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.modules.notification.api.NotificationRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public List<Notification> findAll() throws SQLException {
        String sql = "SELECT * FROM notification ORDER BY id_notif";
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
        String sql = "SELECT * FROM notification WHERE id_notif = ?";
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
        String sql = "INSERT INTO notification (titre, message, date_notif, heure_notif, type, priorite, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, n.getTitre() != null ? n.getTitre().name() : null);
            ps.setString(2, n.getMessage());
            ps.setDate(3, n.getDate() != null ? Date.valueOf(n.getDate()) : null);
            ps.setTime(4, n.getHeure() != null ? Time.valueOf(n.getHeure()) : null);
            ps.setString(5, n.getType() != null ? n.getType().name() : null);
            ps.setString(6, n.getPriorite() != null ? n.getPriorite().name() : null);
            ps.setString(7, n.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    n.setIdNotif(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Notification n) throws SQLException {
        String sql = "UPDATE notification SET titre = ?, message = ?, date_notif = ?, heure_notif = ?, type = ?, priorite = ?, modifie_par = ? WHERE id_notif = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, n.getTitre() != null ? n.getTitre().name() : null);
            ps.setString(2, n.getMessage());
            ps.setDate(3, n.getDate() != null ? Date.valueOf(n.getDate()) : null);
            ps.setTime(4, n.getHeure() != null ? Time.valueOf(n.getHeure()) : null);
            ps.setString(5, n.getType() != null ? n.getType().name() : null);
            ps.setString(6, n.getPriorite() != null ? n.getPriorite().name() : null);
            ps.setString(7, n.getModifiePar());
            ps.setLong(8, n.getIdNotif());

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