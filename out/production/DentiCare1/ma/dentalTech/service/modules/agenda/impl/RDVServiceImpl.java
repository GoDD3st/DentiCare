package ma.dentalTech.service.modules.agenda.impl;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.modules.agenda.api.RDVRepository;
import ma.dentalTech.service.modules.agenda.api.RDVService;
import java.util.List;
import java.util.Optional;

public class RDVServiceImpl implements RDVService {

    private final RDVRepository repository;

    // Injection par constructeur, comme dans PatientServiceImpl
    public RDVServiceImpl(RDVRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RDV> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<RDV> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public RDV create(RDV item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du rendez-vous", e);
        }
    }

    @Override
    public RDV update(Long id, RDV item) {
        try {
            // On définit l'ID avant la mise à jour, correspondant au champ idRDV de l'entité RDV
            item.setIdRDV(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du rendez-vous", e);
        }
    }

    @Override
    public RDV delete(RDV item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du rendez-vous", e);
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