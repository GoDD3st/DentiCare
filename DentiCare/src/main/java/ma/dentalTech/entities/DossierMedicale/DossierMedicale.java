package ma.dentalTech.entities.DossierMedicale;

import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;

import java.time.LocalDate;

import lombok.Data;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;

@Data
@SuperBuilder
    public class DossierMedicale extends BaseEntity {
        private Long idDossier;
        private LocalDate dateDeCreation;

        private Patient patient;
        private SituationFinanciere situationFinanciere;
        private Medecin medecin;

    public static DossierMedicale createTestInstance(Patient patient) {
        return DossierMedicale.builder()
                .dateDeCreation(LocalDate.now())
                .patient(patient)
                .build();
    }
}