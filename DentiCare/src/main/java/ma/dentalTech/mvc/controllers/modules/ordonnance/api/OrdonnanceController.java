package ma.dentalTech.mvc.controllers.modules.ordonnance.api;

import ma.dentalTech.mvc.dto.OrdonnanceDTO;
import java.util.List;

public interface OrdonnanceController {
    void afficherListeOrdonnances();
    void afficherFormulaireOrdonnance();
    void creerOrdonnance(OrdonnanceDTO ordonnanceDTO);
    void modifierOrdonnance(OrdonnanceDTO ordonnanceDTO);
    void supprimerOrdonnance(Long id);
    List<OrdonnanceDTO> rechercherOrdonnances(Long consultationId);
}

