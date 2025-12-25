package ma.dentalTech.service.modules.patient.impl;

import ma.dentalTech.service.modules.patient.api.AntecedentsService;
import ma.dentalTech.repository.modules.patient.api.antecedentsRepo;
import ma.dentalTech.entities.Antecedents.Antecedents;
import java.util.List;
import java.util.Optional;

public class AntecedentsServiceImpl implements AntecedentsService {

    private final antecedentsRepo repository;

    public AntecedentsServiceImpl(antecedentsRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Antecedents> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Antecedents> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Antecedents create(Antecedents item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'antécédent", e);
        }
    }

    @Override
    public Antecedents update(Long id, Antecedents item) {
        try {
            item.setIdAntecedent(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'antécédent", e);
        }
    }

    @Override
    public Antecedents delete(Antecedents item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'antécédent", e);
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