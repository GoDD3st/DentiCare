package ma.dentalTech.service.modules.notification.impl;

import ma.dentalTech.service.modules.notification.api.NotificationService;
import ma.dentalTech.repository.modules.notification.api.NotificationRepository;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public NotificationServiceImpl() {
        this.notificationRepository = ApplicationContext.getBean(NotificationRepository.class);
    }
    
    @Override
    public List<Notification> findAll() throws Exception {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findByID(Long id) throws Exception {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification create(Notification item) {
        try {
            notificationRepository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Notification update(Long id, Notification item) {
        try {
            item.setIdNotif(id);
            notificationRepository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Notification delete(Notification item) {
        try {
            notificationRepository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

