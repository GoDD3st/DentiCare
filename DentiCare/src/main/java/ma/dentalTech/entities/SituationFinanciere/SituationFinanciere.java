package ma.dentalTech.entities.SituationFinanciere;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.enums.SituationStatutEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SituationFinanciere extends BaseEntity {
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private SituationStatutEnum statut;
    private Boolean enPromo;

    private Patient patient;
    private List<Facture> factures;
}