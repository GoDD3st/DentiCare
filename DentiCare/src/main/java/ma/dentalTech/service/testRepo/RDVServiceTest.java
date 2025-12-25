package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.service.modules.agenda.api.RDVService;

public class RDVServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : RDVService ---");
        try {
            RDVService service = ApplicationContext.getBean(RDVService.class);
            RDV rdv = new RDV();
            rdv.setMotif("Consultation Test");
            service.create(rdv);
            System.out.println("✅ RDV créé (ID: " + rdv.getIdRDV() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}