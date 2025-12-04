package ma.dentalTech.entities.Log;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log extends BaseEntity {
    private String action;
    private LocalDateTime dateHeure;
    private String ipAdresse;
    private String status;

    private Utilisateur utilisateur;
}