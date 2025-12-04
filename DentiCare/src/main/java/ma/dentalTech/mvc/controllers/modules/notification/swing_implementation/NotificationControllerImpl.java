package ma.dentalTech.mvc.controllers.modules.notification.swing_implementation;

import ma.dentalTech.mvc.controllers.modules.notification.api.NotificationController;
import ma.dentalTech.service.modules.notification.api.NotificationService;
import ma.dentalTech.mvc.dto.NotificationDTO;
import ma.dentalTech.mvc.ui.modules.notification.NotificationView;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;

public class NotificationControllerImpl implements NotificationController {
    
    private final NotificationService notificationService;
    private NotificationView view;
    
    public NotificationControllerImpl() {
        this.notificationService = ApplicationContext.getBean(NotificationService.class);
    }
    
    @Override
    public void afficherListeNotifications() {
        if (view == null) {
            view = new NotificationView(this);
        }
        view.afficherListe();
        view.setVisible(true);
    }
    
    @Override
    public void afficherFormulaireNotification() {
        if (view == null) {
            view = new NotificationView(this);
        }
        view.afficherFormulaire();
        view.setVisible(true);
    }
    
    @Override
    public void creerNotification(NotificationDTO notificationDTO) {
        try {
            notificationService.create(notificationDTO);
            if (view != null) {
                view.afficherMessage("Notification créée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la création: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void supprimerNotification(Long id) {
        try {
            notificationService.delete(id);
            if (view != null) {
                view.afficherMessage("Notification supprimée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    @Override
    public List<NotificationDTO> getNotificationsUtilisateur(Long utilisateurId) {
        return notificationService.findByUtilisateurId(utilisateurId);
    }
    
    @Override
    public void marquerCommeLue(Long notificationId) {
        // Implémentation pour marquer une notification comme lue
        // Peut nécessiter un champ supplémentaire dans l'entité
    }
    
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.findAll();
    }
}

