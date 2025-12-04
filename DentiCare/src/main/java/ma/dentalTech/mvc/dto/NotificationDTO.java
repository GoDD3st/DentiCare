package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.entities.enums.PrioriteEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private NotificationTypeEnum titre;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private NotificationTypeEnum type;
    private PrioriteEnum priorite;
    private String description;
    private List<Long> utilisateurIds;
    private List<String> utilisateurNoms;
}

