package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String nom;
    private String role;
    private String statut; // "Actif" / "Inactif"
    // Ajoute d'autres champs au besoin (permissions, historique, etc.)

    public UserDTO() {}

    public UserDTO(String nom, String role, String statut) {
        this.nom = nom;
        this.role = role;
        this.statut = statut;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
