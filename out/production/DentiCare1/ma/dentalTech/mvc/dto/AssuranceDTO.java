package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssuranceDTO {
    private String organisme;
    private String tauxRemboursement;

    public AssuranceDTO() {}

    public AssuranceDTO(String organisme, String tauxRemboursement) {
        this.organisme = organisme;
        this.tauxRemboursement = tauxRemboursement;
    }

    public String getOrganisme() { return organisme; }
    public void setOrganisme(String organisme) { this.organisme = organisme; }

    public String getTauxRemboursement() { return tauxRemboursement; }
    public void setTauxRemboursement(String tauxRemboursement) { this.tauxRemboursement = tauxRemboursement; }
}

