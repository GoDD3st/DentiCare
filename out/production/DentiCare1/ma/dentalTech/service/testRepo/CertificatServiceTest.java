package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.service.modules.dossierMedicale.api.CertificatService;
import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;
import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;

public class CertificatServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : CertificatService ---");
        try {
            CertificatService service = ApplicationContext.getBean(CertificatService.class);
            DossierMedicaleService dossierService = ApplicationContext.getBean(DossierMedicaleService.class);
            ConsultationService consultationService = ApplicationContext.getBean(ConsultationService.class);
            
            // Créer un dossier médical de test
            DossierMedicale dossier = DossierMedicale.createTestInstance(null);
            dossier = dossierService.create(dossier);
            
            // Créer une consultation de test
            Consultation consultation = Consultation.createTestInstance(dossier);
            consultation = consultationService.create(consultation);
            
            // Créer le certificat avec les dépendances
            Certificat c = Certificat.createTestInstance(dossier, consultation);
            c = service.create(c);
            System.out.println("✅ Certificat créé (ID: " + c.getIdCertificat() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}