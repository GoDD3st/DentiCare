package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// ====================================
//
// Author : Marouane
//
// ====================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentDTO{
    public long idAntecedent;
    public String nom;
    public String niveauDeRisque;
    private String description;
    public String categorie;
}