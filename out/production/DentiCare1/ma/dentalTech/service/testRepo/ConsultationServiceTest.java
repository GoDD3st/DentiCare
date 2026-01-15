package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;

public class ConsultationServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : ConsultationService ---");
        try {
            ConsultationService service = ApplicationContext.getBean(ConsultationService.class);
            DossierMedicaleService dossierService = ApplicationContext.getBean(DossierMedicaleService.class);
            
            // Créer un dossier médical de test
            DossierMedicale dossier = DossierMedicale.createTestInstance(null);
            dossier = dossierService.create(dossier);
            
            // Créer la consultation avec le dossier
            Consultation c = Consultation.createTestInstance(dossier);
            c = service.create(c);
            System.out.println("✅ Consultation créée (ID: " + c.getIdConsultation() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}