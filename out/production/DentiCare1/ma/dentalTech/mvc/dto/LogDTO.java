package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    private Long idLog;           // Nom exact dans l'entité Log
    private String action;        // Nom exact dans l'entité Log
    private LocalDateTime dateLog; // Nom exact dans l'entité Log
    private String details;       // Nom exact dans l'entité Log
    private LocalDate dateCreation; // Nom exact issu de BaseEntity
}