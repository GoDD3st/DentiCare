package ma.dentalTech.entities.Ordonnance;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Ordonnance extends BaseEntity {
    private Long idOrdonnance;
    private LocalDate date;
    private String note;

    // Simple relationships (matching database schema)
    private DossierMedicale dossierMedicale;
    private Consultation consultation;

    // Complex relationships (for future use)
    private List<DossierMedicale> dossiersMedicales; // n--n
    private List<Consultation> consultations; // n--n

    public static Ordonnance createTestInstance() {
        return Ordonnance.builder()
                .date(LocalDate.now())

                .dossiersMedicales(new ArrayList<>())
                .consultations(new ArrayList<>())
                .build();
    }
}