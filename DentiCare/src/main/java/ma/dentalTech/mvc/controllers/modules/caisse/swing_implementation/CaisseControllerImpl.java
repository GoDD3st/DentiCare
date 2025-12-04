package ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation;

import ma.dentalTech.mvc.controllers.modules.caisse.api.CaisseController;
import ma.dentalTech.service.modules.facture.api.FactureService;
import ma.dentalTech.mvc.dto.FactureDTO;
import ma.dentalTech.mvc.dto.SituationFinanciereDTO;
import ma.dentalTech.mvc.ui.modules.caisse.CaisseView;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;

public class CaisseControllerImpl implements CaisseController {
    
    private final FactureService factureService;
    private CaisseView view;
    
    public CaisseControllerImpl() {
        this.factureService = ApplicationContext.getBean(FactureService.class);
    }
    
    @Override
    public void afficherListeFactures() {
        if (view == null) {
            view = new CaisseView(this);
        }
        view.afficherListeFactures();
        view.setVisible(true);
    }
    
    @Override
    public void afficherFormulaireFacture() {
        if (view == null) {
            view = new CaisseView(this);
        }
        view.afficherFormulaireFacture();
        view.setVisible(true);
    }
    
    @Override
    public void creerFacture(FactureDTO factureDTO) {
        try {
            factureService.create(factureDTO);
            if (view != null) {
                view.afficherMessage("Facture créée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la création: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void modifierFacture(FactureDTO factureDTO) {
        try {
            factureService.update(factureDTO);
            if (view != null) {
                view.afficherMessage("Facture modifiée avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la modification: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void enregistrerPaiement(Long factureId, Double montant) {
        try {
            factureService.enregistrerPaiement(factureId, montant);
            if (view != null) {
                view.afficherMessage("Paiement enregistré avec succès", "Succès");
                view.rafraichirListe();
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de l'enregistrement: " + e.getMessage());
            }
        }
    }
    
    @Override
    public SituationFinanciereDTO getSituationFinanciere(Long patientId) {
        // TODO: Implémenter avec SituationFinanciereService
        return null;
    }
    
    @Override
    public List<FactureDTO> getFacturesParPatient(Long patientId) {
        return factureService.findByPatientId(patientId);
    }
    
    @Override
    public void genererFacturePDF(Long factureId) {
        // TODO: Implémenter la génération PDF avec iTextPDF
        if (view != null) {
            view.afficherMessage("Génération PDF à implémenter", "Info");
        }
    }
    
    public List<FactureDTO> getAllFactures() {
        return factureService.findAll();
    }
}

