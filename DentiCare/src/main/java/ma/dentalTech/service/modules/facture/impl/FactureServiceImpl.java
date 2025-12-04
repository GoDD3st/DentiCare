package ma.dentalTech.service.modules.facture.impl;

import ma.dentalTech.service.modules.facture.api.FactureService;
import ma.dentalTech.repository.modules.facture.api.FactureRepository;
import ma.dentalTech.mvc.dto.FactureDTO;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import ma.dentalTech.conf.ApplicationContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FactureServiceImpl implements FactureService {
    
    private final FactureRepository factureRepository;
    
    public FactureServiceImpl() {
        this.factureRepository = ApplicationContext.getBean(FactureRepository.class);
    }
    
    @Override
    public FactureDTO create(FactureDTO factureDTO) {
        Facture facture = mapToEntity(factureDTO);
        facture = factureRepository.save(facture);
        return mapToDTO(facture);
    }
    
    @Override
    public FactureDTO update(FactureDTO factureDTO) {
        Facture facture = mapToEntity(factureDTO);
        facture = factureRepository.save(facture);
        return mapToDTO(facture);
    }
    
    @Override
    public void delete(Long id) {
        factureRepository.deleteById(id);
    }
    
    @Override
    public Optional<FactureDTO> findById(Long id) {
        return factureRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<FactureDTO> findAll() {
        return factureRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FactureDTO> findByPatientId(Long patientId) {
        return factureRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FactureDTO> findByStatut(String statut) {
        return factureRepository.findByStatut(statut).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public FactureDTO enregistrerPaiement(Long factureId, Double montant) {
        Optional<Facture> factureOpt = factureRepository.findById(factureId);
        if (factureOpt.isEmpty()) {
            throw new RuntimeException("Facture non trouvée");
        }
        
        Facture facture = factureOpt.get();
        double nouveauTotalPaye = facture.getTotalePaye() + montant;
        double nouveauReste = facture.getTotaleFacture() - nouveauTotalPaye;
        
        facture.setTotalePaye(nouveauTotalPaye);
        facture.setReste(nouveauReste);
        
        if (nouveauReste <= 0) {
            facture.setStatut(FactureStatutEnum.PAYEE_INTEGRALEMENT);
        } else if (nouveauTotalPaye > 0) {
            facture.setStatut(FactureStatutEnum.PAYEE_PARTIELLEMENT);
        }
        
        facture = factureRepository.save(facture);
        return mapToDTO(facture);
    }
    
    private Facture mapToEntity(FactureDTO dto) {
        Patient patient = new Patient();
        patient.setIdEntite(dto.getPatientId());
        
        SituationFinanciere situationFinanciere = new SituationFinanciere();
        situationFinanciere.setIdEntite(dto.getSituationFinanciereId());
        
        return Facture.builder()
                .idEntite(dto.getId())
                .totaleFacture(dto.getTotaleFacture())
                .totalePaye(dto.getTotalePaye())
                .reste(dto.getReste())
                .statut(dto.getStatut())
                .dateFacture(dto.getDateFacture() != null ? dto.getDateFacture() : LocalDateTime.now())
                .patient(patient)
                .situationFinanciere(situationFinanciere)
                .build();
    }
    
    private FactureDTO mapToDTO(Facture facture) {
        return FactureDTO.builder()
                .id(facture.getIdEntite())
                .totaleFacture(facture.getTotaleFacture())
                .totalePaye(facture.getTotalePaye())
                .reste(facture.getReste())
                .statut(facture.getStatut())
                .dateFacture(facture.getDateFacture())
                .patientId(facture.getPatient() != null ? facture.getPatient().getIdEntite() : null)
                .situationFinanciereId(facture.getSituationFinanciere() != null ? facture.getSituationFinanciere().getIdEntite() : null)
                .build();
    }
}

