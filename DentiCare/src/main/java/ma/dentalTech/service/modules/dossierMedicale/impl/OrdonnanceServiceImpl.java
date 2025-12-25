package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Ordonnance.
 * Cette classe délègue les opérations CRUD au repository OrdonnanceRepo.
 */
public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepo repository;

    /**
     * Injection du repository par constructeur pour plus de cohérence.
     * @param repository Le repository OrdonnanceRepo
     */
    public OrdonnanceServiceImpl(OrdonnanceRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Ordonnance> findAll() throws SQLException {
        return repository.findAll(); //
    }

    @Override
    public Optional<Ordonnance> findByID(Long aLong) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<Ordonnance> findById(Long id) throws SQLException {
        return repository.findById(id); //
    }

    @Override
    public void create(Ordonnance entity) throws SQLException {
        repository.create(entity); //
    }

    @Override
    public Ordonnance update(Long aLong, Ordonnance item) {
        return null;
    }

    @Override
    public void update(Ordonnance entity) throws SQLException {
        repository.update(entity); //
    }

    @Override
    public void delete(Ordonnance entity) throws SQLException {
        repository.delete(entity); //
    }

    @Override
    public void deleteByID(Long aLong) {

    }

    @Override
    public void deleteById(Long id) throws SQLException {
        repository.deleteById(id); //
    }
}