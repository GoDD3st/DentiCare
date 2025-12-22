package ma.dentalTech.mvc.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActeDTO {
        private Long idActe;
        private String libelle;
        private String categorie;
        private Double prixDeBase;
    }

