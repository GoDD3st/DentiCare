package ma.dentalTech.entities.Medicament;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.enums.FormeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medicament extends BaseEntity {
    private Long idMedicament;
    private String nom;
    private String laboratoire;
    private String type;
    private FormeEnum forme;
    private boolean remboursable;
    private Double prixUnitaire;
    private String description;

}