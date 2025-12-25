package ma.dentalTech.service.modules.dossierMedicale.api;

import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.service.common.MainService;

import java.util.List;
import java.util.Optional;

public interface MedicamentService extends MainService<Medicament, Long> {
    class OrdonnanceServiceImpl implements DossierMedicaleService.OrdonnanceService {
        private final OrdonnanceRepo repository;

        public OrdonnanceServiceImpl(OrdonnanceRepo repository) {
            this.repository = repository;
        }

        @Override
        public List<Ordonnance> findAll() throws Exception {
            return repository.findAll();
        }

        @Override
        public Optional<Ordonnance> findByID(Long id) throws Exception {
            return repository.findById(id);
        }

        @Override
        public Ordonnance create(Ordonnance item) {
            try {
                repository.create(item);
                return item;
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la création de l'ordonnance", e);
            }
        }

        @Override
        public Ordonnance update(Long id, Ordonnance item) {
            try {
                item.setIdOrdonnance(id); //
                repository.update(item);
                return item;
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la mise à jour de l'ordonnance", e);
            }
        }

        @Override
        public Ordonnance delete(Ordonnance item) {
            try {
                repository.delete(item);
                return item;
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la suppression de l'ordonnance", e);
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
}