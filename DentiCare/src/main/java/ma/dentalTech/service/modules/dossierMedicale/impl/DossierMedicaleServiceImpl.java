package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;
import ma.dentalTech.repository.modules.dossierMedicale.api.DossierMedicaleRepo;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class DossierMedicaleServiceImpl implements DossierMedicaleService {

    private final DossierMedicaleRepo repository;

    // Injection via ApplicationContext
    public DossierMedicaleServiceImpl() {
        this.repository = ApplicationContext.getBean(DossierMedicaleRepo.class);
    }

    @Override
    public List<DossierMedicale> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<DossierMedicale> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public DossierMedicale create(DossierMedicale Dm) {
        try {
            // Règle métier : un patient ne peut avoir qu'un seul dossier
            // médical par cabinet (idEntite). On vérifie donc s'il existe
            // déjà un dossier avec le même patient et la même entité.
            if (Dm != null && Dm.getPatient() != null && Dm.getPatient().getIdPatient() != null) {
                Long targetPatientId = Dm.getPatient().getIdPatient();
                Long targetEntiteId = Dm.getIdEntite(); // peut être null si un seul cabinet

                for (DossierMedicale existing : repository.findAll()) {
                    if (existing.getPatient() != null
                            && existing.getPatient().getIdPatient() != null
                            && existing.getPatient().getIdPatient().equals(targetPatientId)) {

                        Long existingEntiteId = existing.getIdEntite();
                        boolean sameEntite =
                                (existingEntiteId == null && targetEntiteId == null)
                                        || (existingEntiteId != null && existingEntiteId.equals(targetEntiteId));

                        if (sameEntite) {
                            throw new RuntimeException(
                                    "Ce patient possède déjà un dossier médical dans ce cabinet.");
                        }
                    }
                }
            }

            repository.create(Dm);
            return Dm;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du dossier médical", e);
        }
    }

    @Override
    public DossierMedicale update(Long id, DossierMedicale Dm) {
        try {
            Dm.setIdDossier(id);
            // Même règle métier que pour la création : empêcher plusieurs dossiers
            // pour le même patient dans le même cabinet, en ignorant le dossier courant.
            if (Dm != null && Dm.getPatient() != null && Dm.getPatient().getIdPatient() != null) {
                Long targetPatientId = Dm.getPatient().getIdPatient();
                Long targetEntiteId = Dm.getIdEntite();

                for (DossierMedicale existing : repository.findAll()) {
                    if (existing.getIdDossier() == null || existing.getIdDossier().equals(id)) {
                        continue; // on ignore le dossier en cours de modification
                    }

                    if (existing.getPatient() != null
                            && existing.getPatient().getIdPatient() != null
                            && existing.getPatient().getIdPatient().equals(targetPatientId)) {

                        Long existingEntiteId = existing.getIdEntite();
                        boolean sameEntite =
                                (existingEntiteId == null && targetEntiteId == null)
                                        || (existingEntiteId != null && existingEntiteId.equals(targetEntiteId));

                        if (sameEntite) {
                            throw new RuntimeException(
                                    "Ce patient possède déjà un dossier médical dans ce cabinet.");
                        }
                    }
                }
            }

            repository.update(Dm);
            return Dm;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du dossier médical", e);
        }
    }

    @Override
    public DossierMedicale delete(DossierMedicale Dm) {
        try {
            repository.delete(Dm);
            return Dm;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du dossier médical", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du dossier médical par ID", e);
        }
    }
}