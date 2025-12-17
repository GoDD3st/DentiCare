package ma.dentalTech.repository.modules.patient.api;

import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    //  Fonctionnalittes optionnelles  aa aajouter  apres le CRUD
    /*List<Patient> findByNom(String nom);
    List<Patient> findByAssurance(String assurance);
    Optional<Patient> findByDossierMedicaleId(Long dossierId); */
}

