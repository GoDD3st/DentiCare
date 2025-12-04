package ma.dentalTech.entities.AgendaMensuel;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.enums.MoisEnum;
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
    private MoisEnum mois;
    private List<Object> joursNonDisponible;

    private Medecin medecin;
}