package ma.dentalTech.entities.Patient;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import ma.dentalTech.entities.RDV.RDV;
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
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private SexeEnum sexe;
    private String adresse;
    private String telephone;
    private AssuranceEnum assurance;

    private DossierMedicale dossierMedicale;
    private SituationFinanciere situationFinanciere;
    private List<RDV> rendezVous;
    private List<Antecedents> antecedents;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient that = (Patient) o;
        return id != null && id.equals(that.id);
    }


@Override
public int hashCode() {
    return id != null ? id.hashCode() : 0;
}

@Override
public String toString() {
    return """
        Patient {
          id = %d,
          nom = '%s',
          prenom = '%s',
          adresse = '%s',
          telephone = '%s',
          dateNaissance = %s,
          sexe = %s,
          assurance = %s,
          antecedentsCount = %d
        }
        """.formatted(
            id,
            nom,
            prenom,
            adresse,
            telephone,
            dateNaissance,
            sexe,
            assurance,
            antecedents == null ? 0 : antecedents.size()
    );
}



public int compareTo(Patient other) {
    return id.compareTo(other.id);
}

}