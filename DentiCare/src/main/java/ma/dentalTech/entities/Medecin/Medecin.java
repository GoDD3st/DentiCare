package ma.dentalTech.entities.Medecin;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.enums.JoursEnum;
import ma.dentalTech.entities.enums.MoisEnum;
import ma.dentalTech.entities.Staff.Staff;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class Medecin extends Staff {
    private Long idMedecin;
    private String specialite;

    private AgendaMensuel JoursNonDisponible;
    private AgendaMensuel agendaMensuel;

    public static Medecin createTestInstance() {
        return Medecin.builder()
                .nom("Alami")
                .specialite("Orthodontie")
                .JoursNonDisponible(AgendaMensuel.builder()
                        .mois(MoisEnum.JANVIER)
                        .joursNonDisponible(List.of(JoursEnum.Samedi))
                        .build())
                .build();
    }
}