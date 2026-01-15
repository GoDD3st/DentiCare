package ma.dentalTech.entities.Antecedents;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.enums.RisqueEnum;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class Antecedents extends BaseEntity {
    private Long idAntecedent;
    private String nom;
    private String categorie;
    private String description;
    private RisqueEnum niveauDeRisque;

    private List<Patient> patients;

    public static Antecedents createTestInstance() {
        return Antecedents.builder()
                .idAntecedent(1L)
                .nom("Hypertension artérielle")
                .categorie("Cardiovasculaire")
                .description("Patient sous traitement, vigilance lors de l'anesthésie locale.")
                .niveauDeRisque(RisqueEnum.MOYEN)
                .patients(new ArrayList<>())
                .build();
    }
}