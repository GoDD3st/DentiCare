package ma.dentalTech.repository.modules.dossierMedicale.api;

import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;

public interface PrescriptionRepo extends CrudRepository<Prescription, Long> {
}

