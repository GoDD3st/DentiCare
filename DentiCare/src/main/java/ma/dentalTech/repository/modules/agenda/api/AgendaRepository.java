package ma.dentalTech.repository.modules.agenda.api;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.repository.common.CrudRepository;
import ma.dentalTech.entities.enums.MoisEnum;
import java.util.List;
import java.util.Optional;

public interface AgendaRepository extends CrudRepository<AgendaMensuel, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
}