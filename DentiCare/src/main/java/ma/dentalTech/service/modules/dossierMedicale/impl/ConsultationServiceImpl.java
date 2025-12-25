package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;
import ma.dentalTech.repository.modules.dossierMedicale.api.ConsultationRepo;
import ma.dentalTech.entities.Consultation.Consultation;
import java.util.List;
import java.util.Optional;

public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepo repository;

    // Injection par constructeur (sans ApplicationContext.getBean)
    public ConsultationServiceImpl(ConsultationRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Consultation> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Consultation> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Consultation create(Consultation item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la consultation", e);
        }
    }

    @Override
    public Consultation update(Long id, Consultation item) {
        try {
            item.setIdConsultation(id); // Utilisation du setter spécifique
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la consultation", e);
        }
    }

    @Override
    public Consultation delete(Consultation item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la consultation", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la consultation par ID", e);
        }
    }
}