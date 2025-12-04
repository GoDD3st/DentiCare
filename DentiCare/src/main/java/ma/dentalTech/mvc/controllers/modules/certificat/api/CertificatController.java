package ma.dentalTech.mvc.controllers.modules.certificat.api;

import ma.dentalTech.mvc.dto.CertificatDTO;
import java.util.List;

public interface CertificatController {
    void afficherListeCertificats();
    void afficherFormulaireCertificat();
    void creerCertificat(CertificatDTO certificatDTO);
    void modifierCertificat(CertificatDTO certificatDTO);
    void supprimerCertificat(Long id);
    List<CertificatDTO> rechercherCertificatsParPatient(Long patientId);
}

