package ma.dentalTech.entities.Medecin;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Staff.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medecin extends Staff {
    private String specialite;
    private AgendaMensuel JoursNonDisponible;

    private AgendaMensuel agendaMensuel;


}