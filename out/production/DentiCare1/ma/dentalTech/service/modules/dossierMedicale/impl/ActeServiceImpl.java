package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.ActeService;
import ma.dentalTech.repository.modules.dossierMedicale.api.ActeRepo;
import ma.dentalTech.entities.Acte.Acte;
import java.util.List;
import java.util.Optional;

public class ActeServiceImpl implements ActeService {

    private final ActeRepo repository;

    public ActeServiceImpl(ActeRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Acte> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Acte> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Acte create(Acte item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'acte", e);
        }
    }

    @Override
    public Acte update(Long id, Acte item) {
        try {
            item.setIdActe(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'acte", e);
        }
    }

    @Override
    public Acte delete(Acte item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'acte", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression par ID", e);
        }
    }
}