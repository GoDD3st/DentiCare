package ma.dentalTech.mvc.controllers.modules.ordonnance.swing_implementation;

import ma.dentalTech.mvc.controllers.modules.ordonnance.api.OrdonnanceController;
import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;
import ma.dentalTech.mvc.dto.OrdonnanceDTO;
import ma.dentalTech.mvc.ui.modules.ordonnance.OrdonnanceView;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;

public class OrdonnanceControllerImpl implements OrdonnanceController {
    
    private final OrdonnanceService ordonnanceService;
    private OrdonnanceView view;
    
    public OrdonnanceControllerImpl() {
        this.ordonnanceService = ApplicationContext.getBean(OrdonnanceService.class);
    }
    
    @Override
    public void afficherListeOrdonnances() {
        if (view == null) {
            view = new OrdonnanceView(this);
        }
        view.afficherListe();
        view.setVisible(true);
    }
    
    @Override
    public void afficherFormulaireOrdonnance() {
        if (view == null) {
            view = new OrdonnanceView(this);
        }
        view.afficherFormulaire();
        view.setVisible(true);
    }
    
    @Override
    public void creerOrdonnance(OrdonnanceDTO ordonnanceDTO) {
        try {
            ordonnanceService.create(ordonnanceDTO);
            if (view != null) {
                view.afficherMessage("Ordonnance créée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la création: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void modifierOrdonnance(OrdonnanceDTO ordonnanceDTO) {
        try {
            ordonnanceService.update(ordonnanceDTO);
            if (view != null) {
                view.afficherMessage("Ordonnance modifiée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void supprimerOrdonnance(Long id) {
        try {
            ordonnanceService.delete(id);
            if (view != null) {
                view.afficherMessage("Ordonnance supprimée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    @Override
    public List<OrdonnanceDTO> rechercherOrdonnances(Long consultationId) {
        return ordonnanceService.findByConsultationId(consultationId);
    }
    
    public List<OrdonnanceDTO> getAllOrdonnances() {
        return ordonnanceService.findAll();
    }
}

