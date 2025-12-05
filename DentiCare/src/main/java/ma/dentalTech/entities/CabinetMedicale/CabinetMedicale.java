package ma.dentalTech.entities.CabinetMedicale;

import ma.dentalTech.common.Adresse;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CabinetMedicale extends BaseEntity {
    private Long idCabinet;
    private String nom;
    private String email;
    private String logo;
    private Adresse adresse;
    private String cin;
    private String tel1;
    private String tel2;
    private String siteWeb;
    private String instagram;
    private String facebook;
    private String description;

    private Patient patient;
    private SituationFinanciere situationFinanciere;
    private Medecin medecin;

}