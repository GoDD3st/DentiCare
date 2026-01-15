package ma.dentalTech.mvc.controllers.modules.caisse.api;

import ma.dentalTech.entities.Caisse.Caisse;

import java.util.List;
import java.util.Optional;

public interface CaisseController {
    List<Caisse> getAllCaisseRecords();
    Optional<Caisse> getCaisseById(Long id);
    Caisse createCaisseRecord(Caisse caisse);
    Caisse updateCaisseRecord(Long id, Caisse caisse);
    void deleteCaisseRecord(Long id);
    Double getTotalAmount();
    Double getTodayAmount();
}
