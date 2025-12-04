package ma.dentalTech.entities.Revenues;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Revenues extends BaseEntity {
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;
}