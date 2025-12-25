package ma.dentalTech.service.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService;
import java.util.List;
import java.util.Optional;

public class CabinetMedicaleServiceImpl implements CabinetMedicaleService {

    private final cabinetMedicaleRepo repository;

    // Injection par constructeur (sans ApplicationContext.getBean)
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
    public CabinetMedicale create(CabinetMedicale item) {
        try {
            repository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du cabinet", e);
        }
    }

    @Override
    public CabinetMedicale update(Long id, CabinetMedicale item) {
        try {
            item.setIdCabinet(id); // Utilisation du setter setIdCabinet de l'entité
            repository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cabinet", e);
        }
    }

    @Override
    public CabinetMedicale delete(CabinetMedicale item) {
        try {
            repository.delete(item);
            return item;
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