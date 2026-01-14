package ma.dentalTech.service.modules.dossierMedicale.api;

import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.service.common.MainService;

import java.util.List;
import java.util.Optional;

public interface MedicamentService extends MainService<Medicament, Long> {

}