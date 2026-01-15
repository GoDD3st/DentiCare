package ma.dentalTech.mvc.controllers.modules.dossiers.api;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;

import java.util.List;
import java.util.Optional;

public interface DossiersController {
    List<DossierMedicale> getAllDossiers();
    Optional<DossierMedicale> getDossierById(Long id);
    DossierMedicale createDossier(DossierMedicale dossier);
    DossierMedicale updateDossier(Long id, DossierMedicale dossier);
    void deleteDossier(Long id);
    List<DossierMedicale> searchDossiers(String searchTerm);
}
