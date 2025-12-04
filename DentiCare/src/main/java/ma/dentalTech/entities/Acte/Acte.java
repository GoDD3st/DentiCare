package ma.dentalTech.entities.Acte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.BaseEntity.BaseEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Acte extends BaseEntity {
    private String libelle;
    private String categorie;
    private Double prixDeBase;
}