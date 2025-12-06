package ma.dentalTech.entities.Caisse;
import ma.dentalTech.entities.enums.ModeEncaissementEnum;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor@NoArgsConstructor@Builder
public class Caisse {
    private Long idCaisse;
    private Double montant;
    private LocalDateTime dateEncassement;
    private ModeEncaissementEnum modeEncaissement;
    private String reference;

}
