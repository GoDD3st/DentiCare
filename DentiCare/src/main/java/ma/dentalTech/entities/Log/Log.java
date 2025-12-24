package ma.dentalTech.entities.Log;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Log extends BaseEntity {
    private Long idLog;
    private String action;
    private LocalDateTime dateHeure;
    private String ipAdresse;
    private String status;

    private Utilisateur utilisateur;
    public static Log createTestInstance() {
        return Log.builder()
                .action("Action de test")
                .dateHeure(LocalDateTime.now())
                .ipAdresse("127.0.0.1")
                .status("OK")
                .build();
    }
}