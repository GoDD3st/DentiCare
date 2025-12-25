package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.service.modules.dossierMedicale.api.MedicamentService;

public class MedicamentServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : MedicamentService ---");
        try {
            MedicamentService service = ApplicationContext.getBean(MedicamentService.class);
            Medicament m = Medicament.createTestInstance();
            service.create(m);
            System.out.println("✅ Médicament créé : " + m.getNom());
        } catch (Exception e) { e.printStackTrace(); }
    }
}