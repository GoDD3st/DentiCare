package ma.dentalTech.service.modules.finance.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.service.modules.facture.api.FactureService;
import ma.dentalTech.repository.modules.finance.api.FactureRepo;
import java.util.List;
import java.util.Optional;

public class FactureServiceImpl implements FactureService {

    private final FactureRepo factureRepository;

    // Mise à jour : Injection par constructeur au lieu de ApplicationContext.getBean
    public FactureServiceImpl(FactureRepo factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public List<Facture> findAll() throws Exception {
        return factureRepository.findAll();
    }

    @Override
    public Optional<Facture> findByID(Long id) throws Exception {
        return factureRepository.findById(id);
    }

    @Override
    public Facture create(Facture item) {
        try {
            factureRepository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la facture", e);
        }
    }

    @Override
    public Facture update(Long id, Facture item) {
        try {
            item.setIdFacture(id); // Utilisation du setter idFacture de l'entité
            factureRepository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la facture", e);
        }
    }

    @Override
    public Facture delete(Facture item) {
        try {
            factureRepository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la facture", e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            factureRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la facture par ID", e);
        }
    }
}