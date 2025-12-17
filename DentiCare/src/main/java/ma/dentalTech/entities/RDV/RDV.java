package ma.dentalTech.entities.RDV;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.enums.RDVStatutEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RDV extends BaseEntity {
    private Long idRDV;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private RDVStatutEnum statut;
    private String noteMedecin;

    private DossierMedicale dossierMedicale;
    private Consultation consultation;
    private Patient patient;
    private Medecin medecin;
}