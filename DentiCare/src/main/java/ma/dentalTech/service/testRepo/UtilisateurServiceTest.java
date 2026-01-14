package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.service.modules.auth.api.UtilisateurService;

public class UtilisateurServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : UtilisateurService ---");
        try {
            UtilisateurService service = ApplicationContext.getBean(UtilisateurService.class);
            Utilisateur u = Utilisateur.createTestInstance();
            u = service.create(u);
            System.out.println("✅ Utilisateur créé (ID: " + u.getIdUser() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}