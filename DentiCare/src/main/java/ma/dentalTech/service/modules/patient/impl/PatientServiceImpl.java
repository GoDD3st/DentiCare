package ma.dentalTech.service.modules.patient.impl;

import ma.dentalTech.service.modules.patient.api.PatientService;
import ma.dentalTech.repository.modules.patient.api.patientRepository;
import ma.dentalTech.entities.Patient.Patient;
import java.util.List;
import java.util.Optional;

public class PatientServiceImpl implements PatientService {

    private final patientRepository repository;

    // Injection par constructeur
    public PatientServiceImpl(patientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Patient> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<Patient> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public Patient create(Patient item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du patient", e);
        }
    }

    @Override
    public Patient update(Long id, Patient item) {
        try {
            item.setIdPatient(id);
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du patient", e);
        }
    }

    @Override
    public Patient delete(Patient item) {
        try {
            repository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du patient", e);
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