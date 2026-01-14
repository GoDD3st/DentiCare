package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AntecedentDTO {
    private String type;
    private String description;

    public AntecedentDTO() {}

    public AntecedentDTO(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
