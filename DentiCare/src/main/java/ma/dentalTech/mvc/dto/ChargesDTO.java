package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChargesDTO {
    private Long id;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime dateCharge;
    private Long idCabinet;
}

