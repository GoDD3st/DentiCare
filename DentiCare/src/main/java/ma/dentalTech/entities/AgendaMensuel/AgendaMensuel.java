package ma.dentalTech.entities.AgendaMensuel;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.enums.MoisEnum;
import ma.dentalTech.entities.enums.JoursEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaMensuel extends BaseEntity {
    private Long idAgenda;
    private MoisEnum mois;
    private List<JoursEnum> joursNonDisponible;

    private Medecin medecin;
}