package ma.dentalTech.repository.common;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Charges.Charges;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.FileAttente.FileAttente;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.entities.Log.Log;
import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.entities.Revenues.Revenues;
import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Secretaire.Secretaire;
import ma.dentalTech.entities.Staff.Staff;
import ma.dentalTech.entities.Statistique.Statistique;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.*;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.common.Adresse;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RowMappers {

    private RowMappers(){}

    // Méthode helper pour les colonnes de BaseEntity
    private static void mapBaseFields(ma.dentalTech.entities.BaseEntity.BaseEntity entity, ResultSet rs) throws SQLException {
        entity.setIdEntite(rs.getLong("id_entite"));
        entity.setDateCreation(rs.getDate("date_creation") != null ? rs.getDate("date_creation").toLocalDate() : null);
        entity.setDateDerniereModification(rs.getTimestamp("date_derniere_modification") != null ? rs.getTimestamp("date_derniere_modification").toLocalDateTime() : null);
        entity.setCreePar(rs.getString("cree_par"));
        entity.setModifiePar(rs.getString("modifie_par"));
    }

    public static Patient mapPatient(ResultSet rs) throws SQLException {
        Patient p = Patient.builder()
                .idPatient(rs.getLong("id_patient"))
                .nom(rs.getString("nom"))
                .dateNaissance(rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null)
                .sexe(rs.getString("sexe") != null ? SexeEnum.valueOf(rs.getString("sexe")) : null)
                .adresse(rs.getString("adresse"))
                .telephone(rs.getString("telephone"))
                .assurance(rs.getString("assurance") != null ? AssuranceEnum.valueOf(rs.getString("assurance")) : null) // Ajouté
                .build();
        mapBaseFields(p, rs);

        return p;
    }

    public static Antecedents mapAntecedents(ResultSet rs) throws SQLException {
        Antecedents an = Antecedents.builder()
                .idAntecedent(rs.getLong("id_antecedent"))
                .nom(rs.getString("nom"))
                .categorie(rs.getString("categorie"))
                .description(rs.getString("description"))
                .niveauDeRisque(rs.getString("niveau_de_risque") != null ? RisqueEnum.valueOf(rs.getString("niveau_de_risque")) : null)
                .build();
        mapBaseFields(an, rs);
        return an;
    }

    public static RDV mapRDV(ResultSet rs) throws SQLException {
        RDV rdv = RDV.builder()
                .idRDV(rs.getLong("id_rdv"))
                .date(rs.getDate("date_rdv") != null ? rs.getDate("date_rdv").toLocalDate() : null)
                .heure(rs.getTime("heure") != null ? rs.getTime("heure").toLocalTime() : null)
                .motif(rs.getString("motif"))
                .statut(rs.getString("statut") != null ? RDVStatutEnum.valueOf(rs.getString("statut")) : null)
                .noteMedecin(rs.getString("noteMedecin"))
                .build();
        mapBaseFields(rdv, rs);

        return rdv;
    }

    public static Consultation mapConsultation(ResultSet rs) throws SQLException {
        Consultation c = Consultation.builder()
                .idConsultation(rs.getLong("id_consultation"))
                .dateConsultation(rs.getDate("date_consultation") != null ? rs.getDate("date_consultation").toLocalDate() : null)
                .heureConsultation(rs.getTime("heure_consultation") != null ? rs.getTime("heure_consultation").toLocalTime() : null)
                .statut(rs.getString("statut") != null ? ConsultationStatutEnum.valueOf(rs.getString("statut")) : null)
                .observationMedecin(rs.getString("observationMedecin"))
                .build();
        mapBaseFields(c, rs);

        return c;
    }

    public static DossierMedicale mapDossierMedicale(ResultSet rs) throws SQLException {
        DossierMedicale dm = DossierMedicale.builder()
                .idDossier(rs.getLong("id_dossier"))
                .dateDeCreation(rs.getTimestamp("date_de_creation") != null ? rs.getTimestamp("date_de_creation").toLocalDateTime().toLocalDate() : null)
                .build();

        // Map base fields with correct column names for dossier_medical table
        dm.setIdEntite(rs.getLong("id_entite"));
        dm.setDateDerniereModification(rs.getTimestamp("date_derniere_modification") != null ? rs.getTimestamp("date_derniere_modification").toLocalDateTime() : null);
        dm.setCreePar(rs.getString("cree_par"));
        dm.setModifiePar(rs.getString("modifie_par"));

        return dm;
    }

    public static Medecin mapMedecin(ResultSet rs) throws SQLException {
        Medecin m = Medecin.builder().build();

        // Champs spécifiques à Medecin
        m.setIdMedecin(rs.getLong("id_medecin"));
        m.setSpecialite(rs.getString("specialite"));

        // Pour l'instant, on ne mappe que les champs disponibles dans la table medecin
        // Les champs de Staff et Utilisateur nécessiteraient un JOIN avec les tables staff et utilisateur
        m.setIdStaff(rs.getLong("id_staff"));

        // Champs de BaseEntity (directement dans la table medecin)
        m.setDateCreation(rs.getDate("date_creation") != null ? rs.getDate("date_creation").toLocalDate() : null);
        m.setDateDerniereModification(rs.getTimestamp("date_derniere_modification") != null ? rs.getTimestamp("date_derniere_modification").toLocalDateTime() : null);
        m.setCreePar(rs.getString("cree_par"));
        m.setModifiePar(rs.getString("modifie_par"));

        return m;
    }

    public static Certificat mapCertificat(ResultSet rs) throws SQLException {
        Certificat cert = Certificat.builder()
                .idCertificat(rs.getLong("id_certificat"))
                .idDossier(rs.getLong("id_dossier_medical"))
                .idConsultation(rs.getLong("id_consultation"))
                .dateDebut(rs.getDate("dateDebut") != null ? rs.getDate("dateDebut").toLocalDate() : null)
                .dateFin(rs.getDate("dateFin") != null ? rs.getDate("dateFin").toLocalDate() : null)
                .duree(rs.getInt("duree"))
                .noteMedecin(rs.getString("noteMedecin"))
                .build();
        mapBaseFields(cert, rs);

        return cert;
    }

    public static Acte mapActe(ResultSet rs) throws SQLException {
        Acte a = Acte.builder()
                .idActe(rs.getLong("id_acte"))
                .libelle(rs.getString("libelle"))
                .categorie(rs.getString("categorie")) // Nouveau champ ajouté
                .prixDeBase(rs.getDouble("prix_de_base"))
                .build();
        mapBaseFields(a, rs);

        return a;
    }

    public static Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        Ordonnance o = Ordonnance.builder()
                .idOrdonnance(rs.getLong("id_ordonnance"))
                .date(rs.getDate("date_ord") != null ? rs.getDate("date_ord").toLocalDate() : null)
                .build();
        mapBaseFields(o, rs);

        return o;
    }

    public static Prescription mapPrescription(ResultSet rs) throws SQLException {
        Prescription p = Prescription.builder()
                .idPrescription(rs.getLong("id_prescription"))
                .quantite(rs.getObject("quantite") != null ? rs.getInt("quantite") : null)
                .frequence(rs.getString("frequence"))
                .dureeEnJours(rs.getObject("duree_en_jours") != null ? rs.getInt("duree_en_jours") : null)
                .build();
        mapBaseFields(p, rs);

        return p;
    }

    public static InterventionMedecin mapInterventionMedecin(ResultSet rs) throws SQLException {
        InterventionMedecin i = InterventionMedecin.builder()
                .idIntervention(rs.getLong("id_intervention"))
                .prixDePatient(rs.getObject("prix_de_patient") != null ? rs.getDouble("prix_de_patient") : null)
                .numDent(rs.getObject("num_dent") != null ? rs.getInt("num_dent") : null)
                .build();
        mapBaseFields(i, rs);

        return i;
    }

    public static Facture mapFacture(ResultSet rs) throws SQLException {
        Facture f = Facture.builder()
                .idFacture(rs.getLong("id_facture"))
                .totaleFacture(rs.getObject("totale_facture") != null ? rs.getDouble("totale_facture") : null)
                .totalePaye(rs.getObject("totale_paye") != null ? rs.getDouble("totale_paye") : null)
                .reste(rs.getObject("reste") != null ? rs.getDouble("reste") : null)
                .statut(rs.getString("statut") != null ? FactureStatutEnum.valueOf(rs.getString("statut")) : null)
                .dateFacture(rs.getTimestamp("date_facture") != null ? rs.getTimestamp("date_facture").toLocalDateTime() : null)
                .build();
        mapBaseFields(f, rs);

        return f;
    }

    public static AgendaMensuel mapAgendaMensuel(ResultSet rs) throws SQLException {
        AgendaMensuel am = AgendaMensuel.builder()
                .idAgenda(rs.getLong("id_agenda"))
                .mois(rs.getString("mois") != null ? MoisEnum.valueOf(rs.getString("mois")) : null)
                .build();
        mapBaseFields(am, rs);

        return am;
    }

    public static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = Utilisateur.builder()
                .idUser(rs.getLong("id_user"))
                .nom(rs.getString("nom"))
                .email(rs.getString("email"))
                .adresse(rs.getString("adresse"))
                .cin(rs.getString("cin"))
                .tel(rs.getString("tel"))
                .sexe(rs.getString("sexe") != null ? SexeEnum.valueOf(rs.getString("sexe")) : null)
                .login(rs.getString("login"))
                .motDePass(rs.getString("motDePass"))
                .lastLoginDate(rs.getDate("lastLoginDate") != null ? rs.getDate("lastLoginDate").toLocalDate() : null)
                .dateNaissance(rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null)
                .build();

        mapBaseFields(u, rs);

        return u;
    }

    public static Role mapRole(ResultSet rs) throws SQLException {
        String libelleStr = rs.getString("libelle");
        RoleEnum libelle = null;
        if (libelleStr != null) {
            try {
                // Essayer d'abord la conversion directe
                libelle = RoleEnum.valueOf(libelleStr);
            } catch (IllegalArgumentException e) {
                // Si ça échoue, essayer avec une conversion insensible à la casse
                for (RoleEnum role : RoleEnum.values()) {
                    if (role.name().equalsIgnoreCase(libelleStr.trim())) {
                        libelle = role;
                        break;
                    }
                }
                // Si toujours pas trouvé, logger l'erreur et utiliser null
                if (libelle == null) {
                    System.err.println("Rôle inconnu dans la BD: '" + libelleStr + "'. Rôles disponibles: " +
                        java.util.Arrays.toString(RoleEnum.values()));
                }
            }
        }
        return Role.builder()
                .idRole(rs.getLong("id_role"))
                .libelle(libelle)
                .build();
    }

    public static Medicament mapMedicament(ResultSet rs) throws SQLException {
        Medicament m = Medicament.builder()
                .idMedicament(rs.getLong("id_medicament"))
                .nom(rs.getString("nom"))
                .description(rs.getString("description"))
                .build();
        mapBaseFields(m, rs);
        return m;
    }

    public static Notification mapNotification(ResultSet rs) throws SQLException {
        Notification n = Notification.builder()
                .idNotif(rs.getLong("id_notif"))
                .message(rs.getString("message"))
                .date(rs.getDate("date_notif") != null ? rs.getDate("date_notif").toLocalDate() : null)
                .heure(rs.getTime("heure_notif") != null ? rs.getTime("heure_notif").toLocalTime() : null)
                .type(rs.getString("type_notif") != null ? NotificationTypeEnum.valueOf(rs.getString("type_notif")) : null)
                .priorite(rs.getString("priorite") != null ? PrioriteEnum.valueOf(rs.getString("priorite")) : null)
                .build();
        mapBaseFields(n, rs);
        return n;
    }

    public static Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin ad = Admin.builder()
                .idUser(rs.getLong("id_utilisateur"))
                .build();
        return ad;
    }

    public static Staff mapStaff(ResultSet rs) throws SQLException {
        Staff s = Staff.builder()
                .idStaff(rs.getLong("id_staff"))
                .salaire(rs.getDouble("salaire"))
                .dateRecrutement(rs.getDate("date_recrutement") != null ? rs.getDate("date_recrutement").toLocalDate() : null)
                .build();
        s.setIdUser(rs.getLong("id_utilisateur"));
        return s;
    }

    public static Caisse mapCaisse(ResultSet rs) throws SQLException {
        return Caisse.builder()
                .idCaisse(rs.getLong("id_caisse"))
                .montant(rs.getDouble("montant"))
                .dateEncassement(rs.getTimestamp("date_encaissement") != null ? rs.getTimestamp("date_encaissement").toLocalDateTime() : null)
                .modeEncaissement(rs.getString("mode_encaissement") != null ? ModeEncaissementEnum.valueOf(rs.getString("mode_encaissement")) : null)
                .reference(rs.getString("reference"))
                .build();
    }

    public static Secretaire mapSecretaire(ResultSet rs) throws SQLException {
        Secretaire sec = Secretaire.builder()
                .idSecretaire(rs.getLong("id_secretaire"))
                .numCNSS(rs.getString("num_cnss"))
                .build();
        sec.setIdStaff(rs.getLong("id_staff"));
        return sec;
    }

    public static Revenues mapRevenues(ResultSet rs) throws SQLException {
        Revenues r = Revenues.builder()
                .idRevenue(rs.getLong("id_revenue"))
                .titre(rs.getString("titre"))
                .montant(rs.getDouble("montant"))
                .date(rs.getTimestamp("date_revenue") != null ? rs.getTimestamp("date_revenue").toLocalDateTime() : null)
                .build();
        mapBaseFields(r, rs);
        return r;
    }

    public static Charges mapCharges(ResultSet rs) throws SQLException {
        Charges ch = Charges.builder()
                .idCharge(rs.getLong("id_charge"))
                .titre(rs.getString("titre"))
                .montant(rs.getDouble("montant"))
                .date(rs.getTimestamp("date_charge") != null ? rs.getTimestamp("date_charge").toLocalDateTime() : null)
                .build();
        mapBaseFields(ch, rs);
        return ch;
    }

    public static CabinetMedicale mapCabinetMedicale(ResultSet rs) throws SQLException {
        // Adresse stockée comme une seule chaîne formatée
        String adresseStr = rs.getString("adresse");
        Adresse adr = null;
        if (adresseStr != null && !adresseStr.trim().isEmpty()) {
            adr = Adresse.builder()
                    .rue(adresseStr) // Stocke l'adresse complète dans le champ rue
                    .ville("")
                    .codePostal("")
                    .région("")
                    .pays("")
                    .build();
        }

        CabinetMedicale cm = CabinetMedicale.builder()
                .idCabinet(rs.getLong("id_cabinet"))
                .nom(rs.getString("nom"))
                .adresse(adr)
                .email(rs.getString("email"))
                .tel1(rs.getString("tel1"))
                .tel2(rs.getString("tel2"))
                .siteWeb(rs.getString("siteWeb"))
                .instagram(rs.getString("instagram"))
                .facebook(rs.getString("facebook"))
                .description(rs.getString("description"))
                .build();
        mapBaseFields(cm, rs);
        return cm;
    }

    public static FileAttente mapFileAttente(ResultSet rs) throws SQLException {
        FileAttente fa = FileAttente.builder()
                .idFileAttente(rs.getLong("id_file_attente"))
                .date(rs.getDate("date_file") != null ? rs.getDate("date_file").toLocalDate() : null)
                .capacite(rs.getInt("capacite"))
                .estOuverte(rs.getBoolean("est_ouverte"))
                .build();
        mapBaseFields(fa, rs);
        return fa;
    }

    public static Log mapLog(ResultSet rs) throws SQLException {
        Log l = Log.builder()
                .idLog(rs.getLong("id_log"))
                .action(rs.getString("action"))
                .dateHeure(rs.getTimestamp("date_heure") != null ? rs.getTimestamp("date_heure").toLocalDateTime() : null)
                .ipAdresse(rs.getString("ip_adresse"))
                .status(rs.getString("status"))
                .build();
        mapBaseFields(l, rs);
        return l;
    }

    public static Statistique mapStatistique(ResultSet rs) throws SQLException {
        Statistique s = Statistique.builder()
                .idStatistique(rs.getLong("id_statistique"))
                .nom(rs.getString("nom"))
                .categorie(rs.getString("categorie") != null ? StatistiqueCategorieEnum.valueOf(rs.getString("categorie")) : null)
                .chiffre(rs.getDouble("chiffre"))
                .dateCalcul(rs.getDate("date_calcul") != null ? rs.getDate("date_calcul").toLocalDate() : null)
                .build();
        mapBaseFields(s, rs);
        return s;
    }
}