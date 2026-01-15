package ma.dentalTech.service.modules.agenda.impl;

import ma.dentalTech.entities.FileAttente.FileAttente;
import ma.dentalTech.repository.modules.agenda.api.fileAttenteRepo;
import ma.dentalTech.service.modules.agenda.api.FileAttenteService;
import java.util.List;
import java.util.Optional;

public class FileAttenteServiceImpl implements FileAttenteService {

    private final fileAttenteRepo repository;

    // Injection par constructeur
    public FileAttenteServiceImpl(fileAttenteRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<FileAttente> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<FileAttente> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public FileAttente create(FileAttente item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la file d'attente", e);
        }
    }

    @Override
    public FileAttente update(Long id, FileAttente item) {
        try {
            item.setIdFileAttente(id); // Définit l'ID correspondant à l'entité FileAttente
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la file d'attente", e);
        }
    }

    @Override
    public FileAttente delete(FileAttente item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la file d'attente", e);
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