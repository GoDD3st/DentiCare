package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.InterventionMedecinService;
import ma.dentalTech.repository.modules.dossierMedicale.api.InterventionMedecinRepo;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import java.util.List;
import java.util.Optional;

public class InterventionMedecinServiceImpl implements InterventionMedecinService {

    private final InterventionMedecinRepo repository;

    // Injection par constructeur
    public InterventionMedecinServiceImpl(InterventionMedecinRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<InterventionMedecin> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<InterventionMedecin> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public InterventionMedecin create(InterventionMedecin item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'intervention", e);
        }
    }

    @Override
    public InterventionMedecin update(Long id, InterventionMedecin item) {
        try {
            item.setIdIntervention(id); // Setter de l'ID spécifique
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'intervention", e);
        }
    }

    @Override
    public InterventionMedecin delete(InterventionMedecin item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'intervention", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'intervention par ID", e);
        }
    }
}