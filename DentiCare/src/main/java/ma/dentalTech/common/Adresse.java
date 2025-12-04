package ma.dentalTech.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * Représente un type complexe pour stocker les informations
 * détaillées d'une adresse géographique (rue, ville, code postal).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Adresse {

    private String rue;
    private String ville;
    private String codePostal;
    private String région;
    private String pays;
}