package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.RDVStatutEnum;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RDVDTO {
    private Long id;
    private LocalDateTime dateHeure;
    private String motif;
    private RDVStatutEnum statut;
    private Long patientId;
    private String patientNomComplet; // Pour l'affichage dans l'agenda
    private Long medecinId;
}