package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.service.modules.patient.api.PatientService;

public class PatientServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : PatientService ---");
        try {
            PatientService service = ApplicationContext.getBean(PatientService.class);
            Patient p = Patient.createTestInstance();
            service.create(p);
            System.out.println("✅ Patient créé (ID: " + p.getIdPatient() + ")");
            System.out.println("✅ Nombre total : " + service.findAll().size());
        } catch (Exception e) { e.printStackTrace(); }
    }
}