package ma.dentalTech.service.modules.facture.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.service.modules.facture.api.FactureService;
import ma.dentalTech.repository.modules.finance.api.FactureRepo;
import java.util.List;
import java.util.Optional;

public class FactureServiceImpl implements FactureService {

    public FactureServiceImpl(FactureRepo repo) {
        this.repo = repo;
    }
    private final FactureRepo repo;

    @Override
    public List<Facture> findAll() throws Exception{
        return repo.findAll();
    }

    @Override
    public Optional<Facture> findByID(Long id) throws Exception{
        return Optional(repo.findById(id));
    }

    @Override
    public Object create(Object item) {
        return null;
    }

    @Override
    public Object update(Object o, Object item) {
        return null;
    }

    @Override
    public Object delete(Object item) {
        return null;
    }

    @Override
    public void deleteByID(Object o) {

    }
}

