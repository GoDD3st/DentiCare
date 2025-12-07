package ma.dentalTech.service.modules.ordonnance.impl;

import ma.dentalTech.entities.*;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;
import ma.dentalTech.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentalTech.mvc.dto.OrdonnanceDTO;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdonnanceServiceImpl implements OrdonnanceService {
    
    private final OrdonnanceRepository ordonnanceRepository;
    
    public OrdonnanceServiceImpl() {
        this.ordonnanceRepository = ApplicationContext.getBean(OrdonnanceRepository.class);
    }
    
    @Override
    public OrdonnanceDTO create(OrdonnanceDTO ordonnanceDTO) {
        Ordonnance ordonnance = mapToEntity(ordonnanceDTO);
        ordonnance = ordonnanceRepository.save(ordonnance);
        return mapToDTO(ordonnance);
    }
    
    @Override
    public OrdonnanceDTO update(OrdonnanceDTO ordonnanceDTO) {
        Ordonnance ordonnance = mapToEntity(ordonnanceDTO);
        ordonnance = ordonnanceRepository.save(ordonnance);
        return mapToDTO(ordonnance);
    }
    
    @Override
    public void delete(Long id) {
        ordonnanceRepository.deleteById(id);
    }
    
    @Override
    public Optional<OrdonnanceDTO> findById(Long id) {
        return ordonnanceRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<OrdonnanceDTO> findAll() {
        return ordonnanceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrdonnanceDTO> findByConsultationId(Long consultationId) {
        return ordonnanceRepository.findAllByConsultationId(consultationId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private Ordonnance mapToEntity(OrdonnanceDTO dto) {
        Consultation consultation = new Consultation();
        consultation.setIdEntite(dto.getConsultationId());
        
        return Ordonnance.builder()
                .idOrdonnance(dto.getId())
                .date(dto.getDate())
                .consultation(consultation)
                .build();
    }
    
    private OrdonnanceDTO mapToDTO(Ordonnance ordonnance) {
        Consultation cons = new Consultation();
        return OrdonnanceDTO.builder()
                .id(ordonnance.getIdEntite())
                .date(ordonnance.getDate())
                .consultationId(ordonnance.getConsultations() != null ? ordonnance.getConsultations().getIdEntite() : null)
                .build();
    }
}

