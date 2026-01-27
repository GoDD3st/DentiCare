package ma.dentalTech.mvc.ui.pages.dashboardPages.pageFactory;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.profileDtos.ProfileData;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ma.dentalTech.mvc.ui.pages.dashboardPages.AdminDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DefaultDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DoctorDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.SecretaryDashboardPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.*;
import ma.dentalTech.mvc.ui.pages.otherPages.ConsultationsPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.OrdonnancesPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.CertificatsPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.ActesDentairesPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.InterventionsPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.SituationsFinancieresPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.FacturesPanel;
import java.util.function.Consumer;

public final class DashboardPanelFactory {

    private DashboardPanelFactory(){}

    public static JComponent create(UserPrincipal principal) {
        RoleEnum role = (principal != null) ? principal.rolePrincipal() : null;

        if (role == null) {
            return new DefaultDashboardPanel(principal);
        }

        return switch (role) {
            case ADMIN      -> new AdminDashboardPanel(principal);
            case MEDECIN    -> new DoctorDashboardPanel(principal);
            case SECRETAIRE -> new SecretaryDashboardPanel(principal);
            default         -> new DefaultDashboardPanel(principal);
        };
    }

    public static JComponent createPanel(ApplicationPages page, UserPrincipal principal) {
        return switch (page) {
            case DASHBOARD -> create(principal);
            case PATIENTS -> new PatientsPanel(principal);
            case CAISSE -> new CaissePanel(principal);
            case CABINET -> new CabinetPanel(principal);
            case USERS -> new UsersPanel(principal);
            case CABINETS -> new CabinetsPanel(principal);
            case DOSSIERS_MEDICAUX -> new DossiersPanel(principal);
            case CONSULTATIONS -> createConsultationsPanel(principal);
            case ORDONNANCES -> createOrdonnancesPanel(principal);
            case CERTIFICATS -> createCertificatsPanel(principal);
            case ACTES_DENTAIRES -> createActesPanel(principal);
            case INTERVENTIONS -> createInterventionsPanel(principal);
            case SITUATIONS_FINANCIERES -> createSituationsFinancieresPanel(principal);
            case FACTURES -> createFacturesPanel(principal);
            case PARAMETRAGE -> create(principal); // paramétrage supprimé, on renvoie vers le dashboard
            case LOGS -> new LogsPanel(principal);
            case NOTIFICATIONS -> new LogsPanel(principal); // notifications supprimées
            case PROFILE -> {
                // Create ProfileData from UserPrincipal (limited data available)
                ProfileData profileData = new ProfileData(
                    principal.id(),
                    principal.nom(),
                    "", // prenom not available in UserPrincipal
                    principal.email(),
                    "", // avatar not available
                    "", // tel not available
                    ""  // adresse not available
                );

                // Create callback that saves profile data using UtilisateurService
                Consumer<ProfileData> onProfileSaved = savedData -> {
                    try {
                        // Get UtilisateurService from ApplicationContext
                        ma.dentalTech.service.modules.auth.api.UtilisateurService utilisateurService = null;
                        try {
                            utilisateurService = ma.dentalTech.conf.ApplicationContext.getBean(
                                ma.dentalTech.service.modules.auth.api.UtilisateurService.class);
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la récupération de UtilisateurService: " + e.getMessage());
                            e.printStackTrace();
                            ma.dentalTech.mvc.ui.palette.alert.Alert.error(null, 
                                "Erreur: Le service utilisateur n'est pas disponible. " + e.getMessage());
                            return;
                        }
                        
                        if (utilisateurService == null) {
                            ma.dentalTech.mvc.ui.palette.alert.Alert.error(null, 
                                "Erreur: Le service utilisateur est null. Vérifiez la configuration.");
                            return;
                        }

                        // Find the existing utilisateur
                        var utilisateurOpt = utilisateurService.findByID(savedData.userId());
                        if (utilisateurOpt.isPresent()) {
                            var utilisateur = utilisateurOpt.get();

                            // Update utilisateur with new profile data
                            var updatedUtilisateur = ma.dentalTech.entities.Utilisateur.Utilisateur.builder()
                                .idUser(utilisateur.getIdUser())
                                .nom(savedData.nom())
                                .email(savedData.email())
                                .tel(savedData.tel())
                                .adresse(savedData.adresse())
                                .login(utilisateur.getLogin())
                                .motDePass(utilisateur.getMotDePass())
                                .sexe(utilisateur.getSexe())
                                .cin(utilisateur.getCin())
                                .lastLoginDate(utilisateur.getLastLoginDate())
                                .dateNaissance(utilisateur.getDateNaissance())
                                .roles(utilisateur.getRoles())
                                .notifications(utilisateur.getNotifications())
                                .build();

                            // Copy BaseEntity fields
                            updatedUtilisateur.setIdEntite(utilisateur.getIdEntite());
                            updatedUtilisateur.setDateCreation(utilisateur.getDateCreation());
                            updatedUtilisateur.setDateDerniereModification(utilisateur.getDateDerniereModification());
                            updatedUtilisateur.setModifiePar(utilisateur.getModifiePar());
                            updatedUtilisateur.setCreePar(utilisateur.getCreePar());

                            // Save the updated utilisateur
                            utilisateurService.update(savedData.userId(), updatedUtilisateur);
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la sauvegarde du profil: " + e.getMessage());
                        ma.dentalTech.mvc.ui.palette.alert.Alert.error(null, "Erreur lors de la sauvegarde du profil: " + e.getMessage());
                    }
                };

                yield new ma.dentalTech.mvc.ui.pages.profilePages.ProfilePanel(null, null, profileData, onProfileSaved);
            }
            default -> new DefaultDashboardPanel(principal);
        };
    }

    // Méthodes privées pour créer les panels médicaux
    private static JComponent createConsultationsPanel(UserPrincipal principal) {
        return new ConsultationsPanel(principal);
    }

    private static JComponent createOrdonnancesPanel(UserPrincipal principal) {
        return new OrdonnancesPanel(principal);
    }

    private static JComponent createCertificatsPanel(UserPrincipal principal) {
        return new CertificatsPanel(principal);
    }

    private static JComponent createActesPanel(UserPrincipal principal) {
        return new ActesDentairesPanel(principal);
    }

    private static JComponent createInterventionsPanel(UserPrincipal principal) {
        return new InterventionsPanel(principal);
    }

    private static JComponent createSituationsFinancieresPanel(UserPrincipal principal) {
        return new SituationsFinancieresPanel(principal);
    }

    private static JComponent createFacturesPanel(UserPrincipal principal) {
        return new FacturesPanel(principal);
    }

    private static JComponent createPlaceholderPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(new java.awt.Color(52, 58, 64));

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        messageLabel.setForeground(new java.awt.Color(108, 117, 125));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);

        return panel;
    }
}
