package ma.dentalTech.entities.Ordonnance;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Prescription.Prescription;
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
    private Long idOrd;
    private LocalDate date;

    private Consultation consultation;
    private List<Prescription> prescriptions;

    @Override
    public int hashCode() {
        return idOrd != null ? idOrd.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
        Ordonnance {
          idOrd = %d,
          date = %s,
          consultation = %s
          prescriptionsCount = %d
        }
        """.formatted(
                idOrd,
                date,
                consultation,
                prescriptions == null ? 0 : prescriptions.size()
        );
    }



    public int compareTo(Ordonnance other) {
        return idOrd.compareTo(other.idOrd);
    }
}