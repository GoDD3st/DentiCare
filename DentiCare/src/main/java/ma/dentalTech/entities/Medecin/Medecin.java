package ma.dentalTech.entities.Medecin;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Staff.Staff;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.entities.Certificat.Certificat;
import java.util.List;
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
    private AgendaMensuel agendaMensuel;

    private List<Consultation> consultations;
    private List<RDV> rendezVous;
    private List<Certificat> certificats;


}