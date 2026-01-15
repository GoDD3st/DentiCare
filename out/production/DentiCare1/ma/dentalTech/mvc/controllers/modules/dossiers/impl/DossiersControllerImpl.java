package ma.dentalTech.mvc.controllers.modules.dossiers.impl;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.mvc.controllers.modules.dossiers.api.DossiersController;
import ma.dentalTech.repository.modules.dossierMedicale.api.DossierMedicaleRepo;
import ma.dentalTech.repository.modules.dossierMedicale.impl.DossierMedicalRepoImpl;
import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;
import ma.dentalTech.service.modules.dossierMedicale.impl.DossierMedicaleServiceImpl;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DossiersControllerImpl implements DossiersController {

    private final DossierMedicaleService dossierService;

    public DossiersControllerImpl() {
        DossierMedicaleRepo repo = new DossierMedicalRepoImpl();
        this.dossierService = new DossierMedicaleServiceImpl(repo);
    }

    @Override
    public List<DossierMedicale> getAllDossiers() {
        try {
            return dossierService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération des dossiers: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    @Override
    public Optional<DossierMedicale> getDossierById(Long id) {
        try {
            return dossierService.findByID(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération du dossier: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    @Override
    public DossierMedicale createDossier(DossierMedicale dossier) {
        try {
            return dossierService.create(dossier);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la création du dossier: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public DossierMedicale updateDossier(Long id, DossierMedicale dossier) {
        try {
            return dossierService.update(id, dossier);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la mise à jour du dossier: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteDossier(Long id) {
        try {
            dossierService.deleteByID(id);
            JOptionPane.showMessageDialog(null,
                    "Dossier supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la suppression du dossier: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<DossierMedicale> searchDossiers(String searchTerm) {
        try {
            List<DossierMedicale> allDossiers = dossierService.findAll();
            String lowerSearchTerm = searchTerm.toLowerCase();
            return allDossiers.stream()
                    .filter(d -> {
                        // Recherche par ID, date, ou autres champs pertinents
                        return String.valueOf(d.getIdDossier()).contains(lowerSearchTerm) ||
                               (d.getDateCreation() != null && 
                                d.getDateCreation().toString().toLowerCase().contains(lowerSearchTerm));
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
