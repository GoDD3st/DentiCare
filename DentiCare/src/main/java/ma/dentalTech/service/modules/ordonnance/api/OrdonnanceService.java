package ma.dentalTech.service.modules.ordonnance.api;

import ma.dentalTech.mvc.dto.OrdonnanceDTO;
import java.util.List;
import java.util.Optional;

public interface OrdonnanceService {
    OrdonnanceDTO create(OrdonnanceDTO ordonnanceDTO);
    OrdonnanceDTO update(OrdonnanceDTO ordonnanceDTO);
    void delete(Long id);
    Optional<OrdonnanceDTO> findById(Long id);
    List<OrdonnanceDTO> findAll();
    List<OrdonnanceDTO> findByConsultationId(Long consultationId);
}

