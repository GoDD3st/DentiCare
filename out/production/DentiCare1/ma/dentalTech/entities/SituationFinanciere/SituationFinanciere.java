package ma.dentalTech.entities.SituationFinanciere;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.enums.SituationStatutEnum;
import lombok.Data;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class SituationFinanciere extends BaseEntity {
    private Long idSituation;

    public static SituationFinanciere createTestInstance() {
        return SituationFinanciere.builder()
                .totaleDesActes(1000.0)
                .totalePaye(800.0)
                .credit(200.0)
                .statut(SituationStatutEnum.EN_RETARD)
                .enPromo(false)
                .build();
    }
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private SituationStatutEnum statut;
    private Boolean enPromo;

    private DossierMedicale dossierMedicale;
}