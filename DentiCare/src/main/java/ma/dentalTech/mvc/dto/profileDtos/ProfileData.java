package ma.dentalTech.mvc.dto.profileDtos;

public record ProfileData(
    Long userId,
    String nom,
    String prenom,
    String email,
    String avatar,
    String tel,
    String adresse
) {
    public ProfileData {
        if (nom == null) nom = "";
        if (prenom == null) prenom = "";
        if (email == null) email = "";
        if (avatar == null) avatar = "";
        if (tel == null) tel = "";
        if (adresse == null) adresse = "";
    }
}

