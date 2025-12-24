package ma.dentalTech.entities.Facture;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import java.time.LocalDateTime;
import lombok.Data;


@Data
@lombok.experimental.SuperBuilder

public class Facture extends BaseEntity {
    private Long idFacture;

    public static Facture createTestInstance() {
        return Facture.builder()
                .totaleFacture(500.0)
                .totalePaye(200.0)
                .reste(300.0)
                .statut(FactureStatutEnum.EN_ATTENTE)
                .dateFacture(java.time.LocalDateTime.now())
                .build();
    }
        private Double totaleFacture;
        private Double totalePaye;
        private Double reste;
        private FactureStatutEnum statut;
        private LocalDateTime dateFacture;

        private SituationFinanciere situationFinanciere;
        private Consultation consultation;

}