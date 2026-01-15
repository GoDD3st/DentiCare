package ma.dentalTech.entities.Consultation;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.enums.ConsultationStatutEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Consultation extends BaseEntity {
    private Long idConsultation;
    private LocalDate dateConsultation;
    private LocalTime heureConsultation;
    private ConsultationStatutEnum statut;
    private String observationMedecin;

    private DossierMedicale dossierMedicale;
    private List<Ordonnance> ordonnances;
    private Certificat certificat;

    public static Consultation createTestInstance(DossierMedicale dossier) {
        return Consultation.builder()
                .dateConsultation(LocalDate.now())
                .heureConsultation(LocalTime.now())
                .statut(ConsultationStatutEnum.EN_COURS)
                .observationMedecin("Patient présente des douleurs dentaires aiguës.")
                .dossierMedicale(dossier)
                .build();
    }
}