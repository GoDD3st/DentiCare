package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenuesDTO {
    private Long idRevenue;         // Nom exact de l'entité Revenues
    private Double montant;          // Nom exact de l'entité Revenues
    private LocalDateTime date;      // Nom exact (LocalDateTime) de l'entité Revenues
    private String titre;            // Nom exact de l'entité Revenues (votre "source")
    private String description;      // Nom exact de l'entité Revenues
    private LocalDate dateCreation;  // Nom exact issu de BaseEntity
}