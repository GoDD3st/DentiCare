package ma.dentalTech.entities.InterventionMedecin;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Acte.Acte;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class InterventionMedecin extends BaseEntity {
    private Long idIntervention;
    private Double prixDePatient;
    private Integer numDent;

    private Consultation consultation;
    private Acte acte;
    public static InterventionMedecin createTestInstance() {
        return InterventionMedecin.builder()
                .prixDePatient(100.0)
                .numDent(1)
                .build();
    }
}