package ma.dentalTech.entities.Staff;

import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import java.time.LocalDate;
import lombok.Data;

@Data
@SuperBuilder
public class Staff extends Utilisateur {
    private Long idStaff;
    private double salaire;
    private double prime;
    private LocalDate dateRecrutement;
    private Integer soldeConge;

    private CabinetMedicale cabinetMedicale;

    public static Staff createTestInstance() {
        return Staff.builder()
                .salaire(10523.22)
                .prime(00.00)
                .dateRecrutement(LocalDate.of(2023, 10, 15))
                .soldeConge(2)
                .build();
    }
}