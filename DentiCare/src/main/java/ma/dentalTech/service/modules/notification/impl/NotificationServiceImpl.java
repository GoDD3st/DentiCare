package ma.dentalTech.service.modules.notification.impl;

import ma.dentalTech.service.modules.notification.api.NotificationService;
import ma.dentalTech.repository.modules.notification.api.NotificationRepository;
import ma.dentalTech.mvc.dto.NotificationDTO;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public NotificationServiceImpl() {
        this.notificationRepository = ApplicationContext.getBean(NotificationRepository.class);
    }
    
    @Override
    public NotificationDTO create(NotificationDTO notificationDTO) {
        Notification notification = mapToEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        
        if (notificationDTO.getUtilisateurIds() != null && !notificationDTO.getUtilisateurIds().isEmpty()) {
            envoyerNotification(notification.getIdEntite(), notificationDTO.getUtilisateurIds());
        }
        
        return mapToDTO(notification);
    }
    
    @Override
    public NotificationDTO update(NotificationDTO notificationDTO) {
        Notification notification = mapToEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return mapToDTO(notification);
    }
    
    @Override
    public void delete(Long id) {
        // Supprimer les associations d'abord
        String deleteAssoc = "DELETE FROM Utilisateur_Notification WHERE notification_id = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteAssoc)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des associations", e);
        }
        notificationRepository.deleteById(id);
    }
    
    @Override
    public Optional<NotificationDTO> findById(Long id) {
        return notificationRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<NotificationDTO> findAll() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<NotificationDTO> findByUtilisateurId(Long utilisateurId) {
        return notificationRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void envoyerNotification(Long notificationId, List<Long> utilisateurIds) {
        String sql = "INSERT INTO Utilisateur_Notification (utilisateur_id, notification_id) VALUES (?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Long userId : utilisateurIds) {
                stmt.setLong(1, userId);
                stmt.setLong(2, notificationId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'envoi de la notification", e);
        }
    }
    
    private Notification mapToEntity(NotificationDTO dto) {
        return Notification.builder()
                .idEntite(dto.getId())
                .titre(dto.getTitre())
                .message(dto.getMessage())
                .date(dto.getDate())
                .time(dto.getTime())
                .type(dto.getType())
                .priorite(dto.getPriorite())
                .description(dto.getDescription())
                .build();
    }
    
    private NotificationDTO mapToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getIdEntite())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .date(notification.getDate())
                .time(notification.getTime())
                .type(notification.getType())
                .priorite(notification.getPriorite())
                .description(notification.getDescription())
                .build();
    }
}

