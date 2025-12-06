package ma.dentalTech.entities.Notification;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.entities.enums.PrioriteEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {
    private Long idNotif;
    private NotificationTypeEnum titre;
    private String message;
    private LocalDate date;
    private LocalTime heure;
    private NotificationTypeEnum type;
    private PrioriteEnum priorite;

    private List<Utilisateur> utilisateurs; // Relation n--n
}