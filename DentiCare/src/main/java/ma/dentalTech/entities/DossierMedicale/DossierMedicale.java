package ma.dentalTech.entities.DossierMedicale;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
    public class DossierMedicale extends BaseEntity {
        private Long idDossier;
        private LocalDate dateDeCreation;

        private Patient patient;
        private SituationFinanciere situationFinanciere;
        private Medecin medecin;
    }