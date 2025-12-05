package ma.dentalTech.entities.SituationFinanciere;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.enums.SituationStatutEnum;
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

    private DossierMedicale dossierMedicale;
}