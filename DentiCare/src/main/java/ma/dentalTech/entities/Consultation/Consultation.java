package ma.dentalTech.entities.Consultation;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.entities.enums.ConsultationStatutEnum;
import java.time.LocalDate;
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
    private LocalDate date;
    private ConsultationStatutEnum statut;
    private String observationMedecin;

    private DossierMedicale dossierMedicale;
    private Medecin medecin;
    private List<Ordonnance> ordonnances;
    private List<InterventionMedecin> interventions;
}