package ma.dentalTech.entities.Certificat;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import java.time.LocalDate;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Certificat extends BaseEntity {
    private Long idCertificat;
    private Long idDossier;
    private Long idConsultation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int duree;
    private String noteMedecin;

    private DossierMedicale dossierMedicale;
    private Consultation consultation;

    public static Certificat createTestInstance(DossierMedicale dossier, Consultation consultation) {
        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(3);

        return Certificat.builder()
                .dateDebut(debut)
                .dateFin(fin)
                .duree(3)
                .noteMedecin("Repos strict requis suite à l'intervention.")

                .idDossier(dossier.getIdDossier())
                .idConsultation(consultation.getIdConsultation())

                .dossierMedicale(dossier)
                .consultation(consultation)
                .build();
    }
}