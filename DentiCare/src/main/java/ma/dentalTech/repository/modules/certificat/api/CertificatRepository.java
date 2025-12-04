package ma.dentalTech.repository.modules.certificat.api;

import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface CertificatRepository extends CrudRepository<Certificat, Long> {
    List<Certificat> findByPatientId(Long patientId);
    List<Certificat> findByMedecinId(Long medecinId);
    Optional<Certificat> findById(Long id);
}

