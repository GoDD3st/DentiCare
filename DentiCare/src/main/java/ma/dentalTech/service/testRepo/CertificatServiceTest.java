package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.service.modules.certificat.api.CertificatService;

public class CertificatServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : CertificatService ---");
        try {
            CertificatService service = ApplicationContext.getBean(CertificatService.class);
            Certificat c = new Certificat();
            service.create(c);
            System.out.println("✅ Certificat créé (ID: " + c.getIdCertificat() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}