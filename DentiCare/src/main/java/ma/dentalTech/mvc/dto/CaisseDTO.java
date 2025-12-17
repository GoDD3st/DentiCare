package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CaisseDTO {
    private Long id;
    private Long idFacture;
    private Double montant;
    private LocalDateTime dateEncaissement;
    private String modeEncaissement;
    private String reference;
}
