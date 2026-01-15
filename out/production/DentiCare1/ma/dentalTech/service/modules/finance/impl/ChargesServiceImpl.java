package ma.dentalTech.service.modules.finance.impl;

import ma.dentalTech.service.modules.finance.api.ChargesService;
import ma.dentalTech.repository.modules.finance.api.ChargesRepo;
import ma.dentalTech.entities.Charges.Charges;
import java.util.List;
import java.util.Optional;

public class ChargesServiceImpl implements ChargesService {

    private final ChargesRepo repository;

    public ChargesServiceImpl(ChargesRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Charges> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Charges> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Charges create(Charges item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la charge", e);
        }
    }

    @Override
    public Charges update(Long id, Charges item) {
        try {
            item.setIdCharge(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la charge", e);
        }
    }

    @Override
    public Charges delete(Charges item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la charge", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la charge par ID", e);
        }
    }
}
