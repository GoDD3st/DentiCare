package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedecamentDTO {
    private String code;
    private String designation;
    private String dosage;

    public MedecamentDTO() {}

    public MedecamentDTO(String code, String designation, String dosage) {
        this.code = code;
        this.designation = designation;
        this.dosage = dosage;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}

