package ma.dentalTech.entities.Utilisateur;

import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.enums.SexeEnum;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@SuperBuilder
public class Utilisateur extends BaseEntity {
    private Long idUser;
    private String nom;
    private String email;
    private String adresse;
    private String cin;
    private String tel;
    private SexeEnum sexe;
    private String login;
    private String motDePass;
    private LocalDate lastLoginDate;
    private LocalDate dateNaissance;

    private List<Role> roles; // Relation n--n
    private List<Notification> notifications; // Relation n--n
    public static Utilisateur createTestInstance() {
        return Utilisateur.builder()
                .nom("Test")
                .email("test@test.com")
                .adresse("Test")
                .cin("1234567890")
                .tel("1234567890")
                .sexe(SexeEnum.MASCULIN)
                .login("test")
                .motDePass("test")
                .build();
    }
}