package ma.dentalTech.entities.Acte;

import lombok.experimental.SuperBuilder;
import lombok.Data;
import ma.dentalTech.entities.BaseEntity.BaseEntity;

@Data
@SuperBuilder
public class Acte extends BaseEntity {
        private Long idActe;
        private String libelle;
        private String categorie;
        private Double prixDeBase;

    public static Acte createTestInstance() {
        return Acte.builder()
                .idActe(1L)
                .libelle("Détartrage complet")
                .categorie("Soins préventifs")
                .prixDeBase(300.0)
                .build();
    }
    }