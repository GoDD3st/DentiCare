package ma.dentalTech.service.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Log.Log;
import ma.dentalTech.repository.modules.cabinetMedicale.api.logRepo;
import ma.dentalTech.service.modules.cabinetMedicale.api.LogService;

import java.util.List;
import java.util.Optional;

public class LogServiceImpl implements LogService {

    private final logRepo repository;

    // Injection par constructeur
    public LogServiceImpl(logRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Log> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Log> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Log create(Log item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du journal (log)", e);
        }
    }

    @Override
    public Log update(Long id, Log item) {
        try {
            item.setIdLog(id); // Setter setIdLog de l'entité Log
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du journal (log)", e);
        }
    }

    @Override
    public Log delete(Log item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du journal (log)", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du journal par ID", e);
        }
    }
}