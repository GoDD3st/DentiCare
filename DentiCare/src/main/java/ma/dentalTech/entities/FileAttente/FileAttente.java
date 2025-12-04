package ma.dentalTech.entities.FileAttente;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.RDV.RDV;
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
public class FileAttente extends BaseEntity {
    private LocalDate date;
    private Integer capacite;
    private Boolean estOuverte;

    private List<RDV> rendezVous;
}