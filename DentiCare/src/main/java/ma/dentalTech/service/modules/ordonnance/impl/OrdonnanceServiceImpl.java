package ma.dentalTech.service.modules.ordonnance.impl;

import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class OrdonnanceServiceImpl implements OrdonnanceService {
    
    private final OrdonnanceRepo ordonnanceRepository;
    
    public OrdonnanceServiceImpl() {
        this.ordonnanceRepository = ApplicationContext.getBean(OrdonnanceRepo.class);
    }
    
    @Override
    public List<Ordonnance> findAll() throws Exception {
        return ordonnanceRepository.findAll();
    }

    @Override
    public Optional<Ordonnance> findByID(Long id) throws Exception {
        return ordonnanceRepository.findById(id);
    }

    @Override
    public Ordonnance create(Ordonnance item) {
        try {
            ordonnanceRepository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ordonnance update(Long id, Ordonnance item) {
        try {
            item.setIdOrdonnance(id);
            ordonnanceRepository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ordonnance delete(Ordonnance item) {
        try {
            ordonnanceRepository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            ordonnanceRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}