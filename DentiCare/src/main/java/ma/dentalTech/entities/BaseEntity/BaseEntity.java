package ma.dentalTech.entities.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import ma.dentalTech.entities.Medecin.Medecin;

@Data
@SuperBuilder
public class BaseEntity {
    private Long idEntite;
    private LocalDate dateCreation;
    private LocalDateTime dateDerniereModification;
    private String modifiePar;
    private String creePar;

    private Medecin medecin;
}