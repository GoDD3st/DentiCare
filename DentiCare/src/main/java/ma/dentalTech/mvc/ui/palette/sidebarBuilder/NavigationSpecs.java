package ma.dentalTech.mvc.ui.palette.sidebarBuilder;

import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

import java.util.ArrayList;
import java.util.List;

import static ma.dentalTech.security.Privileges.*;

public final class NavigationSpecs {

    private NavigationSpecs(){}

    public static List<NavSpec> forPrincipal(UserPrincipal principal) {

        boolean isAdmin   = principal != null && principal.roles() != null && principal.roles().contains(RoleEnum.ADMIN);
        boolean isMedecin = principal != null && principal.roles() != null && principal.roles().contains(RoleEnum.MEDECIN);
        boolean isSecretaire = principal != null && principal.roles() != null && principal.roles().contains(RoleEnum.SECRETAIRE);

        List<NavSpec> items = new ArrayList<>();

        // -------- Général (commun)
        if (isAdmin) {
            items.add(item("Général", "Tableau de bord", "/static/icons/home.png", ApplicationPages.DASHBOARD, null));
        } else if (isMedecin) {
            items.add(item("Général", "Dashboard (statistiques)", "/static/icons/home.png", ApplicationPages.DASHBOARD, null));
        } else {
            items.add(item("Général", "Accueil", "/static/icons/home.png", ApplicationPages.DASHBOARD, null));
        }
        items.add(item("Général", "Mon profil", "/static/icons/profile.png", ApplicationPages.PROFILE, null));
        // Notifications supprimées pour tous (y compris secrétaire)

        // -------- Admin
        if (isAdmin) {
            items.add(item("Administration", "Journaux système", "/static/icons/notif.png", ApplicationPages.LOGS, null));
            items.add(item("Administration", "Gérer les cabinets", "/static/icons/cabinet.png", ApplicationPages.CABINETS, CABINET_ACCESS));
            items.add(item("Administration", "Gérer les utilisateurs", "/static/icons/users.png", ApplicationPages.USERS, USERS_ACCESS));
            // Paramètres système retirés du menu
        }

        // -------- Médecin
        if (isMedecin) {
            items.add(item("Patients", "Gérer les patients", "/static/icons/patient.png", ApplicationPages.PATIENTS, PATIENT_ACCESS));
            items.add(item("Dossiers", "Gérer les dossiers médicaux", "/static/icons/dossier.png", ApplicationPages.DOSSIERS_MEDICAUX, PATIENT_ACCESS));
            items.add(item("Consultations", "Gérer les consultations", "/static/icons/consultation.png", ApplicationPages.CONSULTATIONS, PATIENT_ACCESS));
            items.add(item("Prescriptions", "Gérer les ordonnances", "/static/icons/ordonnance.png", ApplicationPages.ORDONNANCES, PATIENT_ACCESS));
            items.add(item("Documents", "Gérer les certificats", "/static/icons/certificat.png", ApplicationPages.CERTIFICATS, PATIENT_ACCESS));
            items.add(item("Soins", "Gérer les actes dentaires", "/static/icons/acte.png", ApplicationPages.ACTES_DENTAIRES, PATIENT_ACCESS));
            items.add(item("Soins", "Gérer les interventions", "/static/icons/intervention.png", ApplicationPages.INTERVENTIONS, PATIENT_ACCESS));
            items.add(item("Finance", "Situations financières", "/static/icons/finance.png", ApplicationPages.SITUATIONS_FINANCIERES, CAISSE_ACCESS));
            items.add(item("Finance", "Gérer les factures", "/static/icons/facture.png", ApplicationPages.FACTURES, CAISSE_ACCESS));
            items.add(item("Finance", "Gérer la caisse", "/static/icons/caisse.png", ApplicationPages.CAISSE, CAISSE_ACCESS));
            items.add(item("Cabinet", "Infos du cabinet", "/static/icons/cabinet.png", ApplicationPages.CABINET, null));
            // Paramètres retirés du menu pour le médecin
        }

        // -------- Secrétaire
        if (isSecretaire) {
            items.add(item("Patients", "Gérer les patients", "/static/icons/patient.png", ApplicationPages.PATIENTS, PATIENT_ACCESS));
            items.add(item("Agenda", "Consultations / RDV", "/static/icons/consultation.png", ApplicationPages.CONSULTATIONS, PATIENT_ACCESS));
            items.add(item("Finance", "Caisse", "/static/icons/caisse.png", ApplicationPages.CAISSE, CAISSE_ACCESS));
            items.add(item("Finance", "Factures", "/static/icons/facture.png", ApplicationPages.FACTURES, CAISSE_ACCESS));
            items.add(item("Dossiers", "Dossiers médicaux", "/static/icons/dossier.png", ApplicationPages.DOSSIERS_MEDICAUX, PATIENT_ACCESS));
        }

        return items;
    }

    private static NavSpec item(String section, String label, String iconPath,
                                ApplicationPages page, String privilegeOrNull) {
        return new NavSpec(section, label, iconPath, page.name(), privilegeOrNull);
    }
}
