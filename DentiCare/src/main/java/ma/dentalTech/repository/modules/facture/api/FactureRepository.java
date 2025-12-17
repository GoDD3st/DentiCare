package ma.dentalTech.repository.modules.facture.api;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends CrudRepository<Facture, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
    /* List<Facture> findByPatientId(Long patientId);
    List<Facture> findByStatut(String statut);
    Optional<Facture> findById(Long id);
*/
}


