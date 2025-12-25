package ma.dentalTech.service.modules.finance.impl;

import ma.dentalTech.service.modules.finance.api.CaisseService;
import ma.dentalTech.repository.modules.finance.api.CaisseRepo;
import ma.dentalTech.entities.Caisse.Caisse;
import java.util.List;
import java.util.Optional;

public class CaisseServiceImpl implements CaisseService {

    private final CaisseRepo repository;

    // Injection par constructeur
    public CaisseServiceImpl(CaisseRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Caisse> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Caisse> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Caisse create(Caisse item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'enregistrement de caisse", e);
        }
    }

    @Override
    public Caisse update(Long id, Caisse item) {
        try {
            item.setIdCaisse(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'enregistrement de caisse", e);
        }
    }

    @Override
    public Caisse delete(Caisse item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'enregistrement de caisse", e);
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