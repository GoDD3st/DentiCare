package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.mvc.dto.FactureDTO;
import ma.dentalTech.mvc.dto.SituationFinanciereDTO;
import java.util.List;

public interface CaisseController {
    void afficherListeFactures();
    void afficherFormulaireFacture();
    void creerFacture(FactureDTO factureDTO);
    void modifierFacture(FactureDTO factureDTO);
    void enregistrerPaiement(Long factureId, Double montant);
    SituationFinanciereDTO getSituationFinanciere(Long patientId);
    List<FactureDTO> getFacturesParPatient(Long patientId);
    void genererFacturePDF(Long factureId);
}

