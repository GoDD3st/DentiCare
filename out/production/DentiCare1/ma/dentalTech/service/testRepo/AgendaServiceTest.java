package ma.dentalTech.service.testRepo;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.service.modules.agenda.api.AgendaService;

public class AgendaServiceTest {
    public static void main(String[] args) {
        System.out.println("--- Test : AgendaService ---");
        try {
            AgendaService service = ApplicationContext.getBean(AgendaService.class);
            AgendaMensuel am = AgendaMensuel.createTestInstance();
            service.create(am);
            System.out.println("✅ Agenda créé (ID: " + am.getIdAgenda() + ")");
        } catch (Exception e) { e.printStackTrace(); }
    }
}