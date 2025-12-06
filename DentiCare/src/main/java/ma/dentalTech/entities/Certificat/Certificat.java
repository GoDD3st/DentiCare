package ma.dentalTech.entities.Certificat;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificat extends BaseEntity {
    private Long idCertificat;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int duree;
    private String noteMedecin;

    private DossierMedicale dossierMedicale;
    private Consultation consultation;
}