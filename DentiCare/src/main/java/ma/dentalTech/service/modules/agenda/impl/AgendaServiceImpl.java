package ma.dentalTech.service.modules.agenda.impl;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.service.modules.agenda.api.AgendaService;
import java.util.List;
import java.util.Optional;

public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository repository;

    // Injection par constructeur
    public AgendaServiceImpl(AgendaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AgendaMensuel> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<AgendaMensuel> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public AgendaMensuel create(AgendaMensuel item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'agenda", e);
        }
    }

    @Override
    public AgendaMensuel update(Long id, AgendaMensuel item) {
        try {
            item.setIdAgenda(id); // Définit l'ID correspondant à l'entité AgendaMensuel
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'agenda", e);
        }
    }

    @Override
    public AgendaMensuel delete(AgendaMensuel item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'agenda", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression par ID", e);
        }
    }
}