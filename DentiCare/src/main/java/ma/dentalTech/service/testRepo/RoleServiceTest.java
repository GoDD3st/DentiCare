package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.service.modules.auth.api.RoleService;

public class RoleServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : RoleService ---");
        try {
            RoleService service = ApplicationContext.getBean(RoleService.class);
            Role r = new Role();
            r.setNom("ADMIN");
            service.create(r);
            System.out.println("✅ Rôle créé (ID: " + r.getIdRole() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}