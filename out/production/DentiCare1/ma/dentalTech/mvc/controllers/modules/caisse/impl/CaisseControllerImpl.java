package ma.dentalTech.mvc.controllers.modules.caisse.impl;

import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.mvc.controllers.modules.caisse.api.CaisseController;
import ma.dentalTech.repository.modules.finance.api.CaisseRepo;
import ma.dentalTech.repository.modules.finance.impl.CaisseRepoImpl;
import ma.dentalTech.service.modules.finance.api.CaisseService;
import ma.dentalTech.service.modules.finance.impl.CaisseServiceImpl;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class CaisseControllerImpl implements CaisseController {

    private final CaisseService caisseService;

    public CaisseControllerImpl() {
        CaisseRepo repo = new CaisseRepoImpl();
        this.caisseService = new CaisseServiceImpl(repo);
    }

    @Override
    public List<Caisse> getAllCaisseRecords() {
        try {
            return caisseService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération des enregistrements de caisse: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    @Override
    public Optional<Caisse> getCaisseById(Long id) {
        try {
            return caisseService.findByID(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération de l'enregistrement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    @Override
    public Caisse createCaisseRecord(Caisse caisse) {
        try {
            return caisseService.create(caisse);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la création de l'enregistrement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Caisse updateCaisseRecord(Long id, Caisse caisse) {
        try {
            return caisseService.update(id, caisse);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la mise à jour de l'enregistrement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCaisseRecord(Long id) {
        try {
            caisseService.deleteByID(id);
            JOptionPane.showMessageDialog(null,
                    "Enregistrement supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Double getTotalAmount() {
        try {
            List<Caisse> records = caisseService.findAll();
            return records.stream()
                    .mapToDouble(c -> c.getMontant() != null ? c.getMontant() : 0.0)
                    .sum();
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public Double getTodayAmount() {
        // TODO: Implémenter la logique pour calculer le montant du jour
        return 0.0;
    }
}
