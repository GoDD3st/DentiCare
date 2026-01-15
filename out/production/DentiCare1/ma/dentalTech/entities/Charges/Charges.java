package ma.dentalTech.entities.Charges;

import java.time.LocalDateTime; 
import lombok.experimental.SuperBuilder;
import lombok.Data;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;

@Data
@SuperBuilder
public class Charges extends BaseEntity {
    private Long idCharge;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    private CabinetMedicale cabinetmedicale;
}