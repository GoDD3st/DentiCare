package ma.dentalTech.service.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Statistique.Statistique;
import ma.dentalTech.repository.modules.cabinetMedicale.api.statistiqueRepo;
import ma.dentalTech.service.modules.cabinetMedicale.api.StatistiqueService;

import java.util.List;
import java.util.Optional;

public class StatistiqueServiceImpl implements StatistiqueService {

    private final statistiqueRepo repository;

    // Injection par constructeur (recommandé pour la testabilité)
    public StatistiqueServiceImpl(statistiqueRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Statistique> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Statistique> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Statistique create(Statistique item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la statistique", e);
        }
    }

    @Override
    public Statistique update(Long id, Statistique item) {
        try {
            item.setIdStatistique(id); // Nom exact de l'ID dans votre entité
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la statistique", e);
        }
    }

    @Override
    public Statistique delete(Statistique item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la statistique", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la statistique par ID", e);
        }
    }
}