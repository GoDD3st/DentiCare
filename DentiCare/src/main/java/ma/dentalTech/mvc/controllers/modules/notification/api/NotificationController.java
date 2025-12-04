package ma.dentalTech.mvc.controllers.modules.notification.api;

import ma.dentalTech.mvc.dto.NotificationDTO;
import java.util.List;

public interface NotificationController {
    void afficherListeNotifications();
    void afficherFormulaireNotification();
    void creerNotification(NotificationDTO notificationDTO);
    void supprimerNotification(Long id);
    List<NotificationDTO> getNotificationsUtilisateur(Long utilisateurId);
    void marquerCommeLue(Long notificationId);
}

