package ma.dentalTech.entities.Patient;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.entities.enums.SexeEnum;
import ma.dentalTech.entities.enums.AssuranceEnum;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends BaseEntity {
    private Long idPatient;
    private String nom;
    private LocalDate dateNaissance;
    private SexeEnum sexe;
    private String adresse;
    private String telephone;
    private AssuranceEnum assurance;

    private DossierMedicale dossierMedicale;
    private List<Antecedents> antecedents;

}