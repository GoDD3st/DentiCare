package ma.dentalTech.entities.Staff;

import ma.dentalTech.entities.Utilisateur.Utilisateur;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff extends Utilisateur {
    private double salaire;
    private double prime;
    private LocalDate dateRecrutement;
    private Integer soldeConge;
}