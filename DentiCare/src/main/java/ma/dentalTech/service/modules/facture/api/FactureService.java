package ma.dentalTech.service.modules.facture.api;

import ma.dentalTech.mvc.dto.FactureDTO;
import java.util.List;
import java.util.Optional;

public interface FactureService {
    FactureDTO create(FactureDTO factureDTO);
    FactureDTO update(FactureDTO factureDTO);
    void delete(Long id);
    Optional<FactureDTO> findById(Long id);
    List<FactureDTO> findAll();
}

