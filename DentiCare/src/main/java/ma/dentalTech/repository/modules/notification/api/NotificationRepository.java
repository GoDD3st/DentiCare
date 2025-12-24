package ma.dentalTech.repository.modules.notification.api;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}

