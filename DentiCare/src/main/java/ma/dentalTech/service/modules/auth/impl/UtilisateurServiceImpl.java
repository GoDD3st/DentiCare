package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.repository.modules.auth.api.UtilisateurRepo;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.service.modules.auth.api.UtilisateurService;
import ma.dentalTech.conf.ApplicationContext;

import java.util.List;
import java.util.Optional;

public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepo repository;

    // Injection via ApplicationContext
    public UtilisateurServiceImpl() {
        this.repository = ApplicationContext.getBean(UtilisateurRepo.class);
    }

    @Override
    public List<Utilisateur> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Utilisateur> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Utilisateur create(Utilisateur u) {
        try {
            repository.create(u);
            return u;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public Utilisateur update(Long id, Utilisateur u) {
        try {
            u.setIdUser(id);
            repository.update(u);
            return u;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }
    }

    @Override
    public Utilisateur delete(Utilisateur u) {
        try {
            repository.delete(u);
            return u;
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
