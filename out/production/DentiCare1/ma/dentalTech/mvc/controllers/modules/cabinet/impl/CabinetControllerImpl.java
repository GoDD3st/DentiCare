package ma.dentalTech.mvc.controllers.modules.cabinet.impl;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.mvc.controllers.modules.cabinet.api.CabinetController;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.repository.modules.cabinetMedicale.impl.cabinetMedicaleRepoImpl;
import ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService;
import ma.dentalTech.service.modules.cabinetMedicale.impl.CabinetMedicaleServiceImpl;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class CabinetControllerImpl implements CabinetController {

    private final CabinetMedicaleService cabinetService;

    public CabinetControllerImpl() {
        cabinetMedicaleRepo repo = new cabinetMedicaleRepoImpl();
        this.cabinetService = new CabinetMedicaleServiceImpl(repo);
    }

    @Override
    public List<CabinetMedicale> getAllCabinets() {
        try {
            return cabinetService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération des cabinets: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    @Override
    public Optional<CabinetMedicale> getCabinetById(Long id) {
        try {
            return cabinetService.findByID(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    @Override
    public CabinetMedicale createCabinet(CabinetMedicale cabinet) {
        try {
            return cabinetService.create(cabinet);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la création du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CabinetMedicale updateCabinet(Long id, CabinetMedicale cabinet) {
        try {
            return cabinetService.update(id, cabinet);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la mise à jour du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCabinet(Long id) {
        try {
            cabinetService.deleteByID(id);
            JOptionPane.showMessageDialog(null,
                    "Cabinet supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la suppression du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public CabinetMedicale getCurrentCabinet() {
        // TODO: Implémenter la logique pour récupérer le cabinet courant de l'utilisateur
        try {
            List<CabinetMedicale> cabinets = cabinetService.findAll();
            return cabinets.isEmpty() ? null : cabinets.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
