package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService;
import ma.dentalTech.conf.ApplicationContext;

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
     * Constructeur pour ApplicationContext
     */
    public OrdonnanceServiceImpl() {
        this.repository = ApplicationContext.getBean(OrdonnanceRepo.class);
    }

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
    public Ordonnance create(Ordonnance o) throws SQLException {
        try {
            repository.create(o);
            return o;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public Ordonnance update(Long id, Ordonnance o) {
        try {
            o.setIdOrdonnance(id); // Utilisation du setter setIdCabinet de l'entité
            repository.update(o);
            return o;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }    }


    @Override
    public Ordonnance delete(Ordonnance o) throws SQLException {
        try {
            repository.delete(o);
            return o;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du cabinet", e);
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