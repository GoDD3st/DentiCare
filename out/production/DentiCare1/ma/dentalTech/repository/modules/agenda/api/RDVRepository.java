package ma.dentalTech.repository.modules.agenda.api;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface RDVRepository extends CrudRepository<RDV, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
}