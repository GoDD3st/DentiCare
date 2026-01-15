package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActeDTO {
    private String libelle;
    private String code;

    public ActeDTO() {}

    public ActeDTO(String libelle, String code) {
        this.libelle = libelle;
        this.code = code;
    }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
