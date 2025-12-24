package ma.dentalTech.entities.AgendaMensuel;

import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.enums.MoisEnum;
import ma.dentalTech.entities.enums.JoursEnum;

import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class AgendaMensuel extends BaseEntity {
    private Long idAgenda;
    private MoisEnum mois;
    private List<JoursEnum> joursNonDisponible;

    private Medecin medecin;
    
    public static AgendaMensuel createTestInstance() {
        List<JoursEnum> joursBloques = List.of(JoursEnum.Samedi, JoursEnum.Dimanche);
        return AgendaMensuel.builder()
                .idAgenda(1L)
                .mois(MoisEnum.SEPTEMBRE)
                .joursNonDisponible(joursBloques)
                .build();
    }
}