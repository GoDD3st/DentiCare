package ma.dentalTech.mvc.ui.pages.dashboardPages.pageFactory;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.profileDtos.ProfileData;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

import javax.swing.*;
import ma.dentalTech.mvc.ui.pages.dashboardPages.AdminDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DefaultDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DoctorDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.SecretaryDashboardPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.*;
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
            case PARAMETRAGE -> new ParametragePanel(principal);
            case LOGS -> new LogsPanel(principal);
            case NOTIFICATIONS -> {
                // Pour les admins, NOTIFICATIONS redirige vers LogsPanel
                boolean isAdmin = principal != null && principal.roles() != null &&
                    principal.roles().contains(ma.dentalTech.entities.enums.RoleEnum.ADMIN);
                if (isAdmin) {
                    yield new LogsPanel(principal);
                } else {
                    yield new NotificationsPanel(principal);
                }
            }
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
}
