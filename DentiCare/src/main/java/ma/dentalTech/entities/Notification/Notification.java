package ma.dentalTech.entities.Notification;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.entities.enums.PrioriteEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Notification extends BaseEntity {
    private Long idNotif;
    private NotificationTypeEnum titre;
    private String message;
    private LocalDate date;
    private LocalTime heure;
    private NotificationTypeEnum type;
    private PrioriteEnum priorite;

    private List<Utilisateur> utilisateurs; // Relation n--n
    public static Notification createTestInstance() {
        return Notification.builder()
                .titre(NotificationTypeEnum.RAPPEL)
                .message("Rendez-vous prévu pour le 15/12/2025")
                .date(LocalDate.now())
                .heure(LocalTime.now())
                .type(NotificationTypeEnum.RAPPEL)
                .priorite(PrioriteEnum.BASSE)
                .build();
    }

    public void setIdNotification(Long id) {
    }
}