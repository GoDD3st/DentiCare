package ma.dentalTech.repository.modules.agenda.api;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface RDVRepository extends CrudRepository<RDV, Long> {
    List<RDV> findByPatientId(Long patientId);
    List<RDV> findByMedecinId(Long medecinId);
    List<RDV> findByDate(LocalDate date);
    List<RDV> findByStatut(String statut);
}