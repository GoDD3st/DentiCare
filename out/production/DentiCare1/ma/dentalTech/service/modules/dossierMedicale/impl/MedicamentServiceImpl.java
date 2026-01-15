package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.MedicamentService;
import ma.dentalTech.repository.modules.dossierMedicale.api.MedicamentRepo;
import ma.dentalTech.entities.Medicament.Medicament;
import java.util.List;
import java.util.Optional;

public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepo repository;

    // Injection par constructeur
    public MedicamentServiceImpl(MedicamentRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Medicament> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Medicament> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Medicament create(Medicament m) {
        try {
            repository.create(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du médicament", e);
        }
    }

    @Override
    public Medicament update(Long id, Medicament m) {
        try {
            m.setIdMedicament(id);
            repository.update(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du médicament", e);
        }
    }

    @Override
    public Medicament delete(Medicament m) {
        try {
            repository.delete(m);
            return m;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du médicament", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du médicament par ID", e);
        }
    }
}