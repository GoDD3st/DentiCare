package ma.dentalTech.repository.modules.notification.api;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
    /* List<Notification> findByUtilisateurId(Long utilisateurId);
    List<Notification> findByPriorite(String priorite);
    List<Notification> findByType(String type);
    Optional<Notification> findById(Long id); */

}

