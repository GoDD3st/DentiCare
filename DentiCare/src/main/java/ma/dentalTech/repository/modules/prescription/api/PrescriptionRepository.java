package ma.dentalTech.repository.modules.prescription.api;

import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {
    List<Prescription> findByOrdonnanceId(Long ordonnanceId);
    List<Prescription> findByMedicamentId(Long medicamentId);
}

