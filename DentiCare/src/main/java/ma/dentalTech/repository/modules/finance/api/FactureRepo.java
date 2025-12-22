package ma.dentalTech.repository.modules.finance.api;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.repository.common.CrudRepository;

public interface FactureRepo extends CrudRepository<Facture, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
    /* List<Facture> findByPatientId(Long patientId);
    List<Facture> findByStatut(String statut);
    Optional<Facture> findById(Long id);
*/
}


