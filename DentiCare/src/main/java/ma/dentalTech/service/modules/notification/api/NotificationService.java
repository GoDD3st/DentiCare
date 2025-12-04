package ma.dentalTech.service.modules.notification.api;

import ma.dentalTech.mvc.dto.NotificationDTO;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    NotificationDTO create(NotificationDTO notificationDTO);
    NotificationDTO update(NotificationDTO notificationDTO);
    void delete(Long id);
    Optional<NotificationDTO> findById(Long id);
    List<NotificationDTO> findAll();
    List<NotificationDTO> findByUtilisateurId(Long utilisateurId);
    void envoyerNotification(Long notificationId, List<Long> utilisateurIds);
}

