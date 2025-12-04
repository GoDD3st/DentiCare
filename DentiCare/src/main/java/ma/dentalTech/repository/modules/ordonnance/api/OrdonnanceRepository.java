package ma.dentalTech.repository.modules.ordonnance.api;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {
    Optional<Ordonnance> findByConsultationId(Long consultationId);
    List<Ordonnance> findAllByConsultationId(Long consultationId);
}

