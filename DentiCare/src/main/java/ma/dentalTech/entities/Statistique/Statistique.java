package ma.dentalTech.entities.Statistique;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import ma.dentalTech.entities.enums.StatistiqueCategorieEnum;

import java.time.LocalDate;

@Data
@SuperBuilder
public class Statistique extends BaseEntity {
    private Long idStatistique;
    private String nom;
    private StatistiqueCategorieEnum categorie;
    private Double chiffre;
    private LocalDate dateCalcul;

    private CabinetMedicale cabinetMedicale;
    public static Statistique createTestInstance() {
        return Statistique.builder()
                .nom("Statistique de test")
                .categorie(StatistiqueCategorieEnum.FINANCIERE)
                .chiffre(100.0)
                .dateCalcul(LocalDate.now())
                .build();
    }
}