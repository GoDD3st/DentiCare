package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.modules.auth.api.MedecinRepo;
import ma.dentalTech.service.modules.auth.api.MedecinService;
import ma.dentalTech.conf.ApplicationContext;

import java.util.List;
import java.util.Optional;

public class MedecinServiceImpl implements MedecinService {
    private final MedecinRepo repository;

    // Injection via ApplicationContext
    public MedecinServiceImpl() {
        this.repository = ApplicationContext.getBean(MedecinRepo.class);
    }

    @Override
    public List<Medecin> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Medecin> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Medecin create(Medecin m) {
        try {
            repository.create(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public Medecin update(Long id, Medecin m) {
        try {
            m.setIdUser(id);
            repository.update(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }
    }

    @Override
    public Medecin delete(Medecin m) {
        try {
            repository.delete(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du cabinet", e);
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
