package ma.dentalTech.service.modules.finance.api;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.service.common.MainService;

/**
 * Interface pour la gestion des factures.
 * Hérite des méthodes CRUD de MainService.
 */
public interface FactureService extends MainService<Facture, Long> {
}