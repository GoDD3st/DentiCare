package ma.dentalTech.common;

import ma.dentalTech.entities.Log.Log;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.service.modules.cabinetMedicale.api.LogService;
import ma.dentalTech.repository.modules.cabinetMedicale.api.logRepo;
import ma.dentalTech.conf.ApplicationContext;

import java.time.LocalDateTime;

/**
 * Utilitaire pour enregistrer automatiquement les logs système
 */
public class SystemLogger {
    
    private static LogService logService;
    
    private static LogService getLogService() {
        if (logService == null) {
            try {
                // Essayer d'abord de charger le service directement depuis ApplicationContext
                try {
                    logService = ApplicationContext.getBean(LogService.class);
                } catch (Exception e1) {
                    // Si le service n'est pas trouvé, créer manuellement avec le repo
                    logRepo repo = ApplicationContext.getBean("logRepository");
                    logService = new ma.dentalTech.service.modules.cabinetMedicale.impl.LogServiceImpl(repo);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de LogService pour SystemLogger: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return logService;
    }
    
    /**
     * Enregistre automatiquement un log système
     * @param action Description de l'action (ex: "Création d'un patient")
     * @param utilisateur Utilisateur qui a effectué l'action (peut être null)
     * @param statut Statut de l'action (ex: "SUCCES", "ERREUR")
     */
    public static void log(String action, Utilisateur utilisateur, String statut) {
        try {
            LogService ls = getLogService();
            if (ls == null) {
                System.err.println("SystemLogger: LogService non disponible, log non enregistré: " + action);
                return;
            }
            
            // Récupérer l'adresse IP (simplifié pour l'instant)
            String ipAdresse = getClientIpAddress();
            
            Log log = Log.builder()
                .action(action)
                .dateHeure(LocalDateTime.now())
                .ipAdresse(ipAdresse)
                .status(statut != null ? statut : "SUCCES")
                .utilisateur(utilisateur)
                .creePar(utilisateur != null ? utilisateur.getNom() : "Système")
                .build();
            
            ls.create(log);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du log système: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Enregistre un log de succès
     */
    public static void logSuccess(String action, Utilisateur utilisateur) {
        log(action, utilisateur, "SUCCES");
    }
    
    /**
     * Enregistre un log d'erreur
     */
    public static void logError(String action, Utilisateur utilisateur) {
        log(action, utilisateur, "ERREUR");
    }
    
    /**
     * Récupère l'adresse IP du client (simplifié)
     */
    private static String getClientIpAddress() {
        try {
            // Pour une application desktop, on peut utiliser l'adresse locale
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}
