package ma.dentalTech.entities.Consultation;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.enums.ConsultationStatutEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Consultation extends BaseEntity {
    private Long idConsultation;
    private LocalDate dateConsultation;
    private LocalTime heureConsultation;
    private ConsultationStatutEnum statut;
    private String observationMedecin;

    private DossierMedicale dossierMedicale;
    private List<Ordonnance> ordonnances;
    private Certificat certificat;
}