package ma.dentalTech.service.modules.dossierMedicale.api;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.service.common.MainService;

public interface DossierMedicaleService extends MainService<DossierMedicale, Long> {
    interface OrdonnanceService extends MainService<Ordonnance, Long> {
    }
}