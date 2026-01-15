package ma.dentalTech.entities.FileAttente;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import java.time.LocalDate;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class FileAttente extends BaseEntity {
    private Long idFileAttente;
    private LocalDate date;
    private Integer capacite;
    private Boolean estOuverte;
    public static FileAttente createTestInstance() {
        return FileAttente.builder()
                .date(LocalDate.now())
                .capacite(10)
                .estOuverte(true)
                .build();
    }
}