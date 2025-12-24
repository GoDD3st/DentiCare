package ma.dentalTech.entities.Secretaire;

import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.Staff.Staff;
import lombok.Data;

@Data
@SuperBuilder
public class Secretaire extends Staff {
    private Long idSecretaire;
    private String numCNSS;
    private Double commission;

    public static Secretaire createTestInstance() {
        return Secretaire.builder()
                .nom("Benani")
                .login("salma_sec")
                .motDePass("123456") // Sans 'e' comme dans Utilisateur
                .build();
    }
}

