package ma.dentalTech.mvc.controllers.modules.cabinets.impl;

import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.mvc.controllers.modules.cabinets.api.CabinetsController;
import ma.dentalTech.repository.modules.cabinetMedicale.api.cabinetMedicaleRepo;
import ma.dentalTech.repository.modules.cabinetMedicale.impl.cabinetMedicaleRepoImpl;
import ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService;
import ma.dentalTech.service.modules.cabinetMedicale.impl.CabinetMedicaleServiceImpl;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CabinetsControllerImpl implements CabinetsController {

    private final CabinetMedicaleService cabinetService;

    public CabinetsControllerImpl() {
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
    public List<CabinetMedicale> searchCabinets(String searchTerm) {
        try {
            List<CabinetMedicale> allCabinets = cabinetService.findAll();
            String lowerSearchTerm = searchTerm.toLowerCase();
            return allCabinets.stream()
                    .filter(c -> {
                        return (c.getNom() != null && c.getNom().toLowerCase().contains(lowerSearchTerm)) ||
                               (c.getAdresse() != null && c.getAdresse().toString().toLowerCase().contains(lowerSearchTerm)) ||
                               (c.getTel1() != null && c.getTel1().toLowerCase().contains(lowerSearchTerm))  ||
                                (c.getTel2() != null && c.getTel2().toLowerCase().contains(lowerSearchTerm));
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la recherche: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
}
