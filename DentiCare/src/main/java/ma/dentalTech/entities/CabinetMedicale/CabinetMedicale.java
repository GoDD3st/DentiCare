package ma.dentalTech.entities.CabinetMedicale;

import ma.dentalTech.common.Adresse;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;  
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
public class CabinetMedicale extends BaseEntity {
    private Long idCabinet;
    private String nom;
    private String email;
    private String logo;
    private Adresse adresse;
    private String tel1;
    private String tel2;
    private String siteWeb;
    private String instagram;
    private String facebook;
    private String description;

    private Patient patient;
    private SituationFinanciere situationFinanciere;
    private Medecin medecin;

    public static CabinetMedicale createTestInstance() {
        return CabinetMedicale.builder()
                .nom("Cabinet Test")
                .adresse(Adresse.builder()
                        .rue("rue 1")
                        .ville("Rabat")
                        .codePostal("10000")
                        .région("Rabat-Salé")
                        .pays("Maroc")
                        .build())
                .email("contact@cabinet.com")
                .tel1("0522000000")
                .tel2("0511000000")
                .description("Cabinet Description")
                .build();
    }
    }
