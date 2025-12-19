package ma.dentalTech.entities.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.Medecin.Medecin;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    private Long idEntite;
    private LocalDate dateCreation;
    private LocalDateTime dateDerniereModification;
    private String modifiePar;
    private String creePar;

    private Medecin medecin;
}