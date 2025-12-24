package ma.dentalTech.entities.Revenues;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;

@Data
@SuperBuilder

public class Revenues extends BaseEntity {
    private Long idRevenue;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    private CabinetMedicale cabinetMedicale;
    public static Revenues createTestInstance() {
        return Revenues.builder()
                .titre("Revenue de test")
                .description("Revenue de test")
                .montant(100.0)
                .date(LocalDateTime.now())
                .build();
    }
}