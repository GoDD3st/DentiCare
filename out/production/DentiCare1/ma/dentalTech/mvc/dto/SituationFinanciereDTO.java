package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.enums.SituationStatutEnum;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SituationFinanciereDTO {
    private Long id;
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private SituationStatutEnum statut;
    private Boolean enPromo;
    private Long patientId;
    private String patientNom;
    private List<FactureDTO> factures;
}

