package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.SituationFinanciereService;
import ma.dentalTech.repository.modules.dossierMedicale.api.SituationFinanciereRepo;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import java.util.List;
import java.util.Optional;

public class SituationFinanciereServiceImpl implements SituationFinanciereService {

    private final SituationFinanciereRepo repository;

    // Injection par constructeur
    public SituationFinanciereServiceImpl(SituationFinanciereRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<SituationFinanciere> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<SituationFinanciere> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public SituationFinanciere create(SituationFinanciere Sf) {
        try {
            repository.create(Sf);
            return Sf;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la situation financière", e);
        }
    }

    @Override
    public SituationFinanciere update(Long id, SituationFinanciere Sf) {
        try {
            Sf.setIdSituation(id);
            repository.update(Sf);
            return Sf;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la situation financière", e);
        }
    }

    @Override
    public SituationFinanciere delete(SituationFinanciere Sf) {
        try {
            repository.delete(Sf);
            return Sf;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la situation financière", e);
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