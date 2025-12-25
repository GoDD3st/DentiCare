package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;
import ma.dentalTech.repository.modules.dossierMedicale.api.DossierMedicaleRepo;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import java.util.List;
import java.util.Optional;

public class DossierMedicaleServiceImpl implements DossierMedicaleService {

    private final DossierMedicaleRepo repository;

    // Injection par constructeur
    public DossierMedicaleServiceImpl(DossierMedicaleRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<DossierMedicale> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<DossierMedicale> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public DossierMedicale create(DossierMedicale item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du dossier médical", e);
        }
    }

    @Override
    public DossierMedicale update(Long id, DossierMedicale item) {
        try {
            item.setIdDossier(id); // Setter spécifique à l'entité DossierMedicale
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du dossier médical", e);
        }
    }

    @Override
    public DossierMedicale delete(DossierMedicale item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du dossier médical", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du dossier médical par ID", e);
        }
    }
}