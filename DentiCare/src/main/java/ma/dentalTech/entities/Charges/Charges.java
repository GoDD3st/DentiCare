package ma.dentalTech.entities.Charges;

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
public class Charges extends BaseEntity {
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;
}