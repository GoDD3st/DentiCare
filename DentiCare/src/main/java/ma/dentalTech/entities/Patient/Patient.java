package ma.dentalTech.entities.Patient;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.entities.enums.SexeEnum;
import ma.dentalTech.entities.enums.AssuranceEnum;
import java.time.LocalDate;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder

public class Patient extends BaseEntity {
    private Long idPatient;
    private String nom;
    private LocalDate dateNaissance;
    private SexeEnum sexe;
    private String adresse;
    private String email;
    private String telephone;
    private AssuranceEnum assurance;

    private DossierMedicale dossierMedicale; // Relation (Patient)n--1(dossierMedicale)
    private List<Antecedents> antecedents; // Relation n--n

    public static Patient createTestInstance() {
        return Patient.builder()
                .nom("Marouane")
                .dateNaissance((LocalDate.of(2004, 5, 28)))
                .sexe(SexeEnum.HOMME)
                .adresse("Rue 123 Rabat")
                .email("email@gmail.com")
                .telephone("0600000000")
                .assurance(AssuranceEnum.CNSS)
                .build();
    }
}