package ma.dentalTech.service.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.repository.modules.cabinetMedicale.api.notificationRepo;
import ma.dentalTech.service.modules.cabinetMedicale.api.NotificationService;

import java.util.List;
import java.util.Optional;

public class NotificationServiceImpl implements NotificationService {

    private final notificationRepo repository;

    // Injection par constructeur (recommandé)
    public NotificationServiceImpl(notificationRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Notification> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Notification> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Notification create(Notification item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur technique lors de la création de la notification", e);
        }
    }

    @Override
    public Notification update(Long id, Notification item) {
        try {
            item.setIdNotification(id); // Nom exact de votre entité
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur technique lors de la mise à jour de la notification", e);
        }
    }

    @Override
    public Notification delete(Notification item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur technique lors de la suppression de la notification", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur technique lors de la suppression par ID", e);
        }
    }
}