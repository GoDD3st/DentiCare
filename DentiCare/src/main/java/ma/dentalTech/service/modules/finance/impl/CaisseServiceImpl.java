package ma.dentalTech.service.modules.finance.impl;

import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.service.modules.finance.api.CaisseService;
import ma.dentalTech.repository.modules.finance.api.CaisseRepository;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class CaisseServiceImpl implements CaisseService {

    private final CaisseRepository repository;

    public CaisseServiceImpl() {
        this.repository = ApplicationContext.getBean(CaisseRepository.class);
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
    public Caisse create(Caisse caisse) throws Exception {
        repository.create(caisse);
        return caisse;
    }

    @Override
    public Caisse update(Long id, Caisse caisse) throws Exception {
        caisse.setIdCaisse(id);
        repository.update(caisse);
        return caisse;
    }

    @Override
    public Caisse delete(Caisse caisse) throws Exception {
        repository.delete(caisse);
        return caisse;
    }

    @Override
    public void deleteByID(Long id) throws Exception {
        repository.deleteById(id);
    }
}