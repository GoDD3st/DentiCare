package ma.dentalTech.entities.Statistique;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.StatistiqueCategorieEnum;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistique extends BaseEntity {
    private Long idStatistique;
    private String nom;
    private StatistiqueCategorieEnum categorie;
    private Double chiffre;
    private LocalDate dateCalcul;

    private CabinetMedicale cabinetMedicale;
}