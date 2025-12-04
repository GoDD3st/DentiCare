package ma.dentalTech.entities.Antecedents;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.enums.RisqueEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Antecedents extends BaseEntity {
    private String nom;
    private String categorie;
    private RisqueEnum niveauDeRisque;

    private List<Patient> patients;
}