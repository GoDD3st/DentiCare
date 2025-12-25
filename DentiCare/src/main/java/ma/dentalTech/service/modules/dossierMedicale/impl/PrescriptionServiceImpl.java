package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.PrescriptionService;
import ma.dentalTech.repository.modules.dossierMedicale.api.PrescriptionRepo;
import ma.dentalTech.entities.Prescription.Prescription;
import java.util.List;
import java.util.Optional;

public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepo repository;

    // Injection par constructeur
    public PrescriptionServiceImpl(PrescriptionRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Prescription> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Prescription> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Prescription create(Prescription item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la prescription", e);
        }
    }

    @Override
    public Prescription update(Long id, Prescription item) {
        try {
            item.setIdPrescription(id); // Setter spécifique
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la prescription", e);
        }
    }

    @Override
    public Prescription delete(Prescription item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la prescription", e);
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