package ma.dentalTech.service.modules.facture.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.service.modules.facture.api.FactureService;
import ma.dentalTech.repository.modules.finance.api.FactureRepo;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class FactureServiceImpl implements FactureService {

    private final FactureRepo factureRepository;

    public FactureServiceImpl() {
        this.factureRepository = ApplicationContext.getBean(FactureRepo.class);
    }

    @Override
    public List<Facture> findAll() throws Exception {
        return factureRepository.findAll();
    }

    @Override
    public Optional<Facture> findByID(Long id) throws Exception {
        return factureRepository.findById(id);
    }

    @Override
    public Facture create(Facture item) {
        try {
            factureRepository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Facture update(Long id, Facture item) {
        try {
            item.setIdFacture(id);
            factureRepository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Facture delete(Facture item) {
        try {
            factureRepository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            factureRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

