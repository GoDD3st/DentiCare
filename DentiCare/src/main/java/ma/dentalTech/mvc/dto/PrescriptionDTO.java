package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
    private Long medicamentId;
    private String medicamentNom;
    private String medicamentForme;
}

