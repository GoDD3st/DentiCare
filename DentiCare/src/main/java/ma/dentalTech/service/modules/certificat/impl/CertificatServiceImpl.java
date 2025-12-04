package ma.dentalTech.service.modules.certificat.impl;

import ma.dentalTech.service.modules.certificat.api.CertificatService;
import ma.dentalTech.repository.modules.certificat.api.CertificatRepository;
import ma.dentalTech.mvc.dto.CertificatDTO;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CertificatServiceImpl implements CertificatService {
    
    private final CertificatRepository certificatRepository;
    
    public CertificatServiceImpl() {
        this.certificatRepository = ApplicationContext.getBean(CertificatRepository.class);
    }
    
    @Override
    public CertificatDTO create(CertificatDTO certificatDTO) {
        Certificat certificat = mapToEntity(certificatDTO);
        certificat = certificatRepository.save(certificat);
        return mapToDTO(certificat);
    }
    
    @Override
    public CertificatDTO update(CertificatDTO certificatDTO) {
        Certificat certificat = mapToEntity(certificatDTO);
        certificat = certificatRepository.save(certificat);
        return mapToDTO(certificat);
    }
    
    @Override
    public void delete(Long id) {
        certificatRepository.deleteById(id);
    }
    
    @Override
    public Optional<CertificatDTO> findById(Long id) {
        return certificatRepository.findById(id).map(this::mapToDTO);
    }
    
    @Override
    public List<CertificatDTO> findAll() {
        return certificatRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CertificatDTO> findByPatientId(Long patientId) {
        return certificatRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CertificatDTO> findByMedecinId(Long medecinId) {
        return certificatRepository.findByMedecinId(medecinId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private Certificat mapToEntity(CertificatDTO dto) {
        Patient patient = new Patient();
        patient.setIdEntite(dto.getPatientId());
        
        Medecin medecin = new Medecin();
        medecin.setIdEntite(dto.getMedecinId());
        
        return Certificat.builder()
                .idEntite(dto.getId())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .duree(dto.getDuree())
                .noteMedecin(dto.getNoteMedecin())
                .patient(patient)
                .medecin(medecin)
                .build();
    }
    
    private CertificatDTO mapToDTO(Certificat certificat) {
        return CertificatDTO.builder()
                .id(certificat.getIdEntite())
                .dateDebut(certificat.getDateDebut())
                .dateFin(certificat.getDateFin())
                .duree(certificat.getDuree())
                .noteMedecin(certificat.getNoteMedecin())
                .patientId(certificat.getPatient() != null ? certificat.getPatient().getIdEntite() : null)
                .medecinId(certificat.getMedecin() != null ? certificat.getMedecin().getIdEntite() : null)
                .build();
    }
}

