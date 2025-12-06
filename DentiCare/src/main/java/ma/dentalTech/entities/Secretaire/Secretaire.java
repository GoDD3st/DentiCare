package ma.dentalTech.entities.Secretaire;

import ma.dentalTech.entities.Staff.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Secretaire extends Staff {
    private Long idSecretaire;
    private String numCNSS;
    private Double commission;
}