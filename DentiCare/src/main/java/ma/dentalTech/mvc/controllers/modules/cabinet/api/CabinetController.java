package ma.dentalTech.mvc.controllers.modules.cabinet.api;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;

import java.util.List;
import java.util.Optional;

public interface CabinetController {
    List<CabinetMedicale> getAllCabinets();
    Optional<CabinetMedicale> getCabinetById(Long id);
    CabinetMedicale createCabinet(CabinetMedicale cabinet);
    CabinetMedicale updateCabinet(Long id, CabinetMedicale cabinet);
    void deleteCabinet(Long id);
    CabinetMedicale getCurrentCabinet();
}
