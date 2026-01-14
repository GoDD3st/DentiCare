package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.repository.modules.auth.api.AdminRepo;
import ma.dentalTech.repository.modules.auth.api.UtilisateurRepo;
import ma.dentalTech.service.modules.auth.api.AdminService;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService {
    private final AdminRepo repository;

    public AdminServiceImpl(AdminRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Admin> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Admin> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Admin create(Admin a) {
        try {
            repository.create(a);
            return a;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public Admin update(Long id, Admin a) {
        try {
            a.setIdUser(id);
            repository.update(a);
            return a;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }
    }

    @Override
    public Admin delete(Admin a) {
        try {
            repository.delete(a);
            return a;
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
