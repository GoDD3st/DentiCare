package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.ConsultationStatutEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {
    private Long idConsultation;
    private LocalDate dateConsultation;
    private LocalTime heureConsultation;
    private ConsultationStatutEnum statut;
    private String observationMedecin;
}