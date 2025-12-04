package ma.dentalTech.mvc.controllers.modules.certificat.swing_implementation;

import ma.dentalTech.mvc.controllers.modules.certificat.api.CertificatController;
import ma.dentalTech.service.modules.certificat.api.CertificatService;
import ma.dentalTech.mvc.dto.CertificatDTO;
import ma.dentalTech.mvc.ui.modules.certificat.CertificatView;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;

public class CertificatControllerImpl implements CertificatController {
    
    private final CertificatService certificatService;
    private CertificatView view;
    
    public CertificatControllerImpl() {
        this.certificatService = ApplicationContext.getBean(CertificatService.class);
    }
    
    @Override
    public void afficherListeCertificats() {
        if (view == null) {
            view = new CertificatView(this);
        }
        view.afficherListe();
        view.setVisible(true);
    }
    
    @Override
    public void afficherFormulaireCertificat() {
        if (view == null) {
            view = new CertificatView(this);
        }
        view.afficherFormulaire();
        view.setVisible(true);
    }
    
    @Override
    public void creerCertificat(CertificatDTO certificatDTO) {
        try {
            certificatService.create(certificatDTO);
            if (view != null) {
                view.afficherMessage("Certificat créé avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la création: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void modifierCertificat(CertificatDTO certificatDTO) {
        try {
            certificatService.update(certificatDTO);
            if (view != null) {
                view.afficherMessage("Certificat modifié avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void supprimerCertificat(Long id) {
        try {
            certificatService.delete(id);
            if (view != null) {
                view.afficherMessage("Certificat supprimé avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    @Override
    public List<CertificatDTO> rechercherCertificatsParPatient(Long patientId) {
        return certificatService.findByPatientId(patientId);
    }
    
    public List<CertificatDTO> getAllCertificats() {
        return certificatService.findAll();
    }
}

