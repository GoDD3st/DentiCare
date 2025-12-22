package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.JoursEnum;
import ma.dentalTech.entities.enums.MoisEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
    public class AgendaMensuelDTO {
        private Long idAgenda;
        private MoisEnum mois;
        private List<JoursEnum> joursNonDisponible;
        private Long idMedecin;
}
