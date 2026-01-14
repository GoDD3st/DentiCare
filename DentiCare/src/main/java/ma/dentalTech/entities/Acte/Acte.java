package ma.dentalTech.entities.Acte;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.BaseEntity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Acte extends BaseEntity {
        private Long idActe;
        private String libelle;
        private String categorie;
        private Double prixDeBase;

    public static Acte createTestInstance() {
        Acte acte = new Acte();
        acte.setIdActe(1L);
        acte.setLibelle("Détartrage complet");
        acte.setCategorie("Soins préventifs");
        acte.setPrixDeBase(300.0);
        return acte;
    }
    }