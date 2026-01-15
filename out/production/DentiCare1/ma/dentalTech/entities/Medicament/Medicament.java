package ma.dentalTech.entities.Medicament;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.enums.FormeEnum;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Medicament extends BaseEntity {
    public static Medicament createTestInstance() {
        return Medicament.builder()
                .nom("Paracétamol")
                .laboratoire("Sanofi")
                .type("Antalgique")
                .forme(null)
                .remboursable(false)
                .prixUnitaire(12.0)
                .description("Utilisé contre la douleur et la fièvre.")
                .build();
    }
    private Long idMedicament;
    private String nom;
    private String laboratoire;
    private String type;
    private FormeEnum forme;
    private boolean remboursable;
    private Double prixUnitaire;
    private String description;

}