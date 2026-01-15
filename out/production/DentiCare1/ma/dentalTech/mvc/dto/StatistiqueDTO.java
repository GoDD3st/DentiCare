package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
// ====================================
//
// Author : Marouane
//
// ====================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueDTO {
    private Long id;
    private String nom;
    private String categorie; // FINANCIERE, PATIENT, CONSULTATION, PERFORMANCE
    private Double chiffre;
    private LocalDate dateCalcul;
    private Long idCabinet;
}
