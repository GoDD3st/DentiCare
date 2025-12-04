package ma.dentalTech.entities.Facture;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Facture extends BaseEntity {
    private Double totaleFacture;
    private Double totalePaye;
    private Double reste;
    private FactureStatutEnum statut;
    private LocalDateTime dateFacture;

    private Patient patient;
    private SituationFinanciere situationFinanciere;
}