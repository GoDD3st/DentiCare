package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.common.Adresse;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CabinetMedicaleDTO {
    private Long idCabinet;           // Nom exact de l'entité CabinetMedicale
    private String nom;               // Nom exact de l'entité CabinetMedicale
    private String telephone;         // Nom exact de l'entité CabinetMedicale
    private String email;             // Nom exact de l'entité CabinetMedicale
    private String matriculeFiscale;  // Nom exact de l'entité CabinetMedicale
    private byte[] logo;              // Nom exact de l'entité CabinetMedicale
    private Adresse adresse;          // Nom exact de l'entité CabinetMedicale
    private LocalDate dateCreation;   // Nom exact issu de BaseEntity
}