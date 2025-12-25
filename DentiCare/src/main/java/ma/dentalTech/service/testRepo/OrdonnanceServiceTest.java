package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.service.modules.ordonnance.api.OrdonnanceService;

public class OrdonnanceServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : OrdonnanceService ---");
        try {
            OrdonnanceService service = ApplicationContext.getBean(OrdonnanceService.class);
            Ordonnance o = Ordonnance.createTestInstance();
            service.create(o);
            System.out.println("✅ Ordonnance créée (ID: " + o.getIdOrdonnance() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}