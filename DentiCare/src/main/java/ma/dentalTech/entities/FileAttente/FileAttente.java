package ma.dentalTech.entities.FileAttente;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileAttente extends BaseEntity {
    private Long idFileAttente;
    private LocalDate date;
    private Integer capacite;
    private Boolean estOuverte;
}