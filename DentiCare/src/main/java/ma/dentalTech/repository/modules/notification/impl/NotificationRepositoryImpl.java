package ma.dentalTech.repository.modules.notification.impl;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.modules.notification.api.NotificationRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.entities.enums.PrioriteEnum;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationRepositoryImpl implements NotificationRepository {
    
    @Override
    public Optional<Notification> findById(Long id) {
        String sql = "SELECT * FROM Notification WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la notification", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Notification> findAll() {
        String sql = "SELECT * FROM Notification";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des notifications", e);
        }
        return notifications;
    }
    
    @Override
    public Notification save(Notification notification) {
        if (notification.getIdEntite() == null) {
            return insert(notification);
        } else {
            return update(notification);
        }
    }
    
    private Notification insert(Notification notification) {
        String sql = "INSERT INTO Notification (titre, `message`, `date`, `time`, `type`, priorite, `description`, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, notification.getTitre().name());
            stmt.setString(2, notification.getMessage());
            stmt.setDate(3, Date.valueOf(notification.getDate()));
            stmt.setTime(4, Time.valueOf(notification.getTime()));
            stmt.setString(5, notification.getType().name());
            stmt.setString(6, notification.getPriorite().name());
            stmt.setString(7, notification.getDescription());
            stmt.setDate(8, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                notification.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la notification", e);
        }
        return notification;
    }
    
    private Notification update(Notification notification) {
        String sql = "UPDATE Notification SET titre = ?, `message` = ?, `date` = ?, `time` = ?, `type` = ?, priorite = ?, `description` = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notification.getTitre().name());
            stmt.setString(2, notification.getMessage());
            stmt.setDate(3, Date.valueOf(notification.getDate()));
            stmt.setTime(4, Time.valueOf(notification.getTime()));
            stmt.setString(5, notification.getType().name());
            stmt.setString(6, notification.getPriorite().name());
            stmt.setString(7, notification.getDescription());
            stmt.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(9, notification.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la notification", e);
        }
        return notification;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Notification WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la notification", e);
        }
    }
    
    @Override
    public List<Notification> findByUtilisateurId(Long utilisateurId) {
        String sql = "SELECT n.* FROM Notification n " +
                    "INNER JOIN Utilisateur_Notification un ON n.idEntite = un.notification_id " +
                    "WHERE un.utilisateur_id = ?";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des notifications", e);
        }
        return notifications;
    }
    
    @Override
    public List<Notification> findByPriorite(String priorite) {
        String sql = "SELECT * FROM Notification WHERE priorite = ?";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, priorite);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des notifications", e);
        }
        return notifications;
    }
    
    @Override
    public List<Notification> findByType(String type) {
        String sql = "SELECT * FROM Notification WHERE `type` = ?";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapToNotification(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des notifications", e);
        }
        return notifications;
    }
    
    private Notification mapToNotification(ResultSet rs) throws SQLException {
        return Notification.builder()
                .idEntite(rs.getLong("idEntite"))
                .titre(NotificationTypeEnum.valueOf(rs.getString("titre")))
                .message(rs.getString("message"))
                .date(rs.getDate("date").toLocalDate())
                .time(rs.getTime("time").toLocalTime())
                .type(NotificationTypeEnum.valueOf(rs.getString("type")))
                .priorite(PrioriteEnum.valueOf(rs.getString("priorite")))
                .description(rs.getString("description"))
                .build();
    }
}

