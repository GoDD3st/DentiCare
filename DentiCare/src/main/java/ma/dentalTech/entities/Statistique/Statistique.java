package ma.dentalTech.entities.Statistique;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistique extends BaseEntity {
    private Integer totalPatient;
    private Integer totalConsultation;
    private Integer totalAnnulations;
    private Double revenueMensuel;
    private Double revenueAnnuel;

    private CabinetMedicale cabinetMedicale;
}