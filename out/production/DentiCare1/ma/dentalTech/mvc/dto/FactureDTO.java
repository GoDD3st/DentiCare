package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import java.time.LocalDateTime;

// ====================================
//
// Author : Marouane
//
// ====================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {
    private Long id;
    private Double totaleFacture;
    private Double totalePaye;
    private Double reste;
    private FactureStatutEnum statut;
    private LocalDateTime dateFacture;
    private Long patientId;
    private String patientNom;
    private Long situationFinanciereId;
    private Double situationCredit;
}

