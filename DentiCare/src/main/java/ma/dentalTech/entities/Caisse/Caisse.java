package ma.dentalTech.entities.Caisse;
import ma.dentalTech.entities.enums.ModeEncaissementEnum;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor@NoArgsConstructor@SuperBuilder
public class Caisse {
    public static Caisse createTestInstance() {
        return Caisse.builder()
                .montant(250.5)
                .dateEncassement(java.time.LocalDateTime.now())
                .modeEncaissement(null)
                .reference("REF-2024-01")
                .build();
    }
    private Long idCaisse;
    private Double montant;
    private LocalDateTime dateEncassement;
    private ModeEncaissementEnum modeEncaissement;
    private String reference;

}
