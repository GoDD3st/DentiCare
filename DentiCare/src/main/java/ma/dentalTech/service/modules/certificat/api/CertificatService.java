package ma.dentalTech.service.modules.certificat.api;

import ma.dentalTech.mvc.dto.CertificatDTO;
import java.util.List;
import java.util.Optional;

public interface CertificatService {
    CertificatDTO create(CertificatDTO certificatDTO);
    CertificatDTO update(CertificatDTO certificatDTO);
    void delete(Long id);
    Optional<CertificatDTO> findById(Long id);
    List<CertificatDTO> findAll();
    List<CertificatDTO> findByPatientId(Long patientId);
    List<CertificatDTO> findByMedecinId(Long medecinId);
}

