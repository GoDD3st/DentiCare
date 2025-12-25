package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;

public class ConsultationServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : ConsultationService ---");
        try {
            ConsultationService service = ApplicationContext.getBean(ConsultationService.class);
            Consultation c = new Consultation();
            service.create(c);
            System.out.println("✅ Consultation créée (ID: " + c.getIdConsultation() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}