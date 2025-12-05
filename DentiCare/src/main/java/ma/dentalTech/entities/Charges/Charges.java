package ma.dentalTech.entities.Charges;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Charges extends BaseEntity {
    private Long idCharge;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    private CabinetMedicale cabinetmedicale;
}