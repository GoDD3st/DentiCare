package ma.dentalTech.mvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {
    private String nom;
    private String description;
    private String permissions; // À détailler si besoin (ex: Set<String> permissions)
    private int nbUtilisateurs;

    public RoleDTO() {}

    public RoleDTO(String nom, String description, String permissions, int nbUtilisateurs) {
        this.nom = nom;
        this.description = description;
        this.permissions = permissions;
        this.nbUtilisateurs = nbUtilisateurs;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    public int getNbUtilisateurs() { return nbUtilisateurs; }
    public void setNbUtilisateurs(int nbUtilisateurs) { this.nbUtilisateurs = nbUtilisateurs; }
}

