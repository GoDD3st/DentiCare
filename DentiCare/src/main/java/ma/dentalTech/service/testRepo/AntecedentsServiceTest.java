package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.service.modules.patient.api.AntecedentsService;

public class AntecedentsServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : AntecedentsService ---");
        try {
            AntecedentsService service = ApplicationContext.getBean(AntecedentsService.class);
            Antecedents a = Antecedents.createTestInstance();
            service.create(a);
            System.out.println("✅ Antécédent créé (ID: " + a.getId() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}