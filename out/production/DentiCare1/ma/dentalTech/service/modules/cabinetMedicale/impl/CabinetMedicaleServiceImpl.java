package ma.dentalTech.service.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService;
import java.util.List;
import java.util.Optional;

public class CabinetMedicaleServiceImpl implements CabinetMedicaleService {

    private final cabinetMedicaleRepo repository;

    public CabinetMedicaleServiceImpl(cabinetMedicaleRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<CabinetMedicale> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Optional<CabinetMedicale> findByID(Long id) throws Exception {
        return repository.findById(id);
    }

    @Override
    public CabinetMedicale create(CabinetMedicale c) {
        try {
            repository.create(c);
            return c;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public CabinetMedicale update(Long id, CabinetMedicale c) {
        try {
            c.setIdCabinet(id); // Utilisation du setter setIdCabinet de l'entité
            repository.update(c);
            return c;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }
    }

    @Override
    public CabinetMedicale delete(CabinetMedicale c) {
        try {
            repository.delete(c);
            return c;
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