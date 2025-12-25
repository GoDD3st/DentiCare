package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.service.modules.auth.api.UtilisateurService;

public class UtilisateurServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : UtilisateurService ---");
        try {
            UtilisateurService service = ApplicationContext.getBean(UtilisateurService.class);
            Utilisateur u = new Utilisateur();
            u.setUsername("admin_test");
            u.setPassword("password123");
            service.create(u);
            System.out.println("✅ Utilisateur créé (ID: " + u.getIdUtilisateur() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}