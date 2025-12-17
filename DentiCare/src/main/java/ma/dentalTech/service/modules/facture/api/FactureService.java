package ma.dentalTech.service.modules.facture.api;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.service.common.MainService;

import java.util.Optional;

public interface FactureService extends MainService {

    Optional<Facture> findByID(Long id) throws Exception;
}

