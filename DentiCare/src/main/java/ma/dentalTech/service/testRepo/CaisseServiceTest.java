package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.service.modules.finance.api.CaisseService;

public class CaisseServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : CaisseService ---");
        try {
            CaisseService service = ApplicationContext.getBean(CaisseService.class);
            Caisse c = Caisse.createTestInstance();
            service.create(c);
            System.out.println("✅ Caisse créée (REF: " + c.getReference() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}