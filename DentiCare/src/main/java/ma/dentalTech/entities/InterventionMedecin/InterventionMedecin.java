package ma.dentalTech.entities.InterventionMedecin;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Acte.Acte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterventionMedecin extends BaseEntity {
    private Double prixDePatient;
    private Integer numDent;

    private Consultation consultation;
    private Acte acte;
}