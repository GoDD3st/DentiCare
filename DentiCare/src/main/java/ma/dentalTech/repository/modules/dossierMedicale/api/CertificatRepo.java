package ma.dentalTech.repository.modules.dossierMedicale.api;

import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.Optional;

public interface CertificatRepo extends CrudRepository<Certificat, Long> {
    Optional<Certificat> findById(Long id);
}

