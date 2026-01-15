package ma.dentalTech.repository.modules.medecin.api;

import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;

public interface MedecinRepository extends CrudRepository<Medecin, Long> {
    List<Medecin> findBySpecialite(String specialite);
}
