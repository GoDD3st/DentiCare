package ma.dentalTech.entities.Utilisateur;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.enums.SexeEnum;
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

    private List<Role> roles;
    private List<Notification> notifications;
}