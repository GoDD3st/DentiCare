package ma.dentalTech.entities.Ordonnance;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
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
public class Ordonnance extends BaseEntity {
    private Long idOrdonnance;
    private LocalDate date;

    private List<DossierMedicale> dossiersMedicales; // n--n
    private List<Consultation> consultations; // n--n
}