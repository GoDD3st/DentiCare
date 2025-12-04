package ma.dentalTech.entities.Medicament;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.entities.enums.FormeEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medicament extends BaseEntity {
    private Long idMed;
    private String nom;
    private String laboratoire;
    private String type;
    private FormeEnum forme;
    private boolean remboursable;
    private Double prixUnitaire;
    private String description;

    private List<Prescription> prescriptions;

    @Override
    public int hashCode() {
        return idMed != null ? idMed.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
        Medicament {
          idNotif = %d,
          nom = %s,
          laboratoire = %s,
          type = %s,
          forme = %s,
          remboursable = %d,
          prixUnitaire = %d,
          description = %s,
          prescriptionsCount = %d
        }
        """.formatted(
                idMed,
                nom,
                laboratoire,
                type,
                forme,
                remboursable,
                prixUnitaire,
                description,
                prescriptions == null ? 0 : prescriptions.size()
        );
    }



    public int compareTo(Medicament other) {
        return idMed.compareTo(other.idMed);
    }
}