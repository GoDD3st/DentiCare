package ma.dentalTech.repository.modules.medecin.api;

import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface MedecinRepository extends CrudRepository<Medecin, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
    // List<Medecin> findBySpecialite(String specialite);
}
