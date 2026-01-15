package ma.dentalTech.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Adresse {
    public static Adresse createTestInstance() {
        return Adresse.builder()
                .rue("Example Rue")
                .ville("Rabat")
                .codePostal("10000")
                .région("Rabat-Salé")
                .pays("Maroc")
                .build();
    }

    private String rue;
    private String ville;
    private String codePostal;
    private String région;
    private String pays;
}