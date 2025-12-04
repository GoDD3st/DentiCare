package ma.dentalTech.entities.DossierMedicale;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Consultation.Consultation;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DossierMedicale extends BaseEntity {
    private LocalDate dateDeCreation;

    private Patient patient;
    private List<Consultation> consultations;
}