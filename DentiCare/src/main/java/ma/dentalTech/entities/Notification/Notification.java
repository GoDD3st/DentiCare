package ma.dentalTech.entities.Notification;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
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
    private LocalTime time;
    private NotificationTypeEnum type;
    private PrioriteEnum priorite;
    private String description;

    private List<Utilisateur> utilisateurs;

    @Override
    public int hashCode() {
        return idNotif != null ? idNotif.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
        Notification {
          idNotif = %d,
          titre = %s,
          message = %s,
          date = %s,
          time = %s,
          type = %s,
          priorite = %s,
          description = %s,
          userCount = %d
        }
        """.formatted(
                idNotif,
                titre,
                message,
                date,
                time,
                type,
                priorite,
                description,
                utilisateurs == null ? 0 : utilisateurs.size()
        );
    }



    public int compareTo(Notification other) {
        return idNotif.compareTo(other.idNotif);
    }
}