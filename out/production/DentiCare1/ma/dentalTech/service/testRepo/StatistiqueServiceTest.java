package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Statistique.Statistique;
import ma.dentalTech.service.modules.cabinetMedicale.api.StatistiqueService;

public class StatistiqueServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : StatistiqueService ---");
        try {
            StatistiqueService service = ApplicationContext.getBean(StatistiqueService.class);
            Statistique s = Statistique.createTestInstance();
            service.create(s);
            System.out.println("✅ Statistique créée : " + s.getNom());
        } catch (Exception e) { e.printStackTrace(); }
    }
}