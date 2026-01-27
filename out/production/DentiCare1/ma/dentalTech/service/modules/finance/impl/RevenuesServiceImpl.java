package ma.dentalTech.service.modules.finance.impl;

import ma.dentalTech.service.modules.finance.api.RevenuesService;
import ma.dentalTech.repository.modules.finance.api.RevenuesRepo;
import ma.dentalTech.entities.Revenues.Revenues;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class RevenuesServiceImpl implements RevenuesService {

    private final RevenuesRepo repository;

    // Injection via ApplicationContext
    public RevenuesServiceImpl() {
        this.repository = ApplicationContext.getBean(RevenuesRepo.class);
    }

    @Override
    public List<Revenues> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Revenues> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Revenues create(Revenues item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du revenu", e);
        }
    }

    @Override
    public Revenues update(Long id, Revenues item) {
        try {
            item.setIdRevenue(id); // Utilisation du nom de setter exact
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du revenu", e);
        }
    }

    @Override
    public Revenues delete(Revenues item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du revenu", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du revenu par ID", e);
        }
    }
}