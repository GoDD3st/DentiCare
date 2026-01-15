package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.FormeEnum;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentDTO {
    // Attributs mappés sur l'entité Medicament
    private Long idMedicament;      // Correspond à idMedicament
    private String nom;             // Correspond à nom
    private String description;     // Correspond à description
    private String laboratoire;     // Correspond à laboratoire
    private String type;            // Correspond à type
    private Double prixUnitaire;    // Correspond à prixUnitaire
    private FormeEnum forme;        // Correspond à forme
    private boolean remboursable;   // Correspond à remboursable

    // Attribut issu de BaseEntity
    private LocalDate dateCreation; // Correspond à dateCreation (type LocalDate dans l'entité)
}