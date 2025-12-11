package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.modules.medecin.api.MedecinRepository;

import java.util.List;
import java.util.Optional;

public class MedicamentRepositoryImpl implements MedecinRepository {
    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        return List.of();
    }

    @Override
    public Optional<Medecin> findById(Long aLong) throws InterruptedException {
        return Optional.empty();
    }

    @Override
    public List<Medecin> findAll() {
        return List.of();
    }

    @Override
    public Medecin save(Medecin entity) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
