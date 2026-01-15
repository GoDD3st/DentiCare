CREATE TABLE IF NOT EXISTS BaseEntity (
    id_entite BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cree_par VARCHAR(100),
    modifie_par VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS utilisateur (
    id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    adresse VARCHAR(255),
    cin VARCHAR(20),
    tel VARCHAR(20),
    sexe ENUM('HOMME', 'FEMME', 'AUTRE'),
    login VARCHAR(50) NOT NULL,
    motDePass VARCHAR(255) NOT NULL,
    lastLoginDate DATETIME,
    dateNaissance DATE,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    INDEX idx_utilisateur_email (email),
    INDEX idx_utilisateur_login (login),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS `admin` (
    id_user BIGINT PRIMARY KEY,
    FOREIGN KEY (id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cabinet_medical (
    id_cabinet BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    logo VARCHAR(255),
    adresse VARCHAR(255),
    tel1 VARCHAR(20),
    tel2 VARCHAR(20),
    siteWeb VARCHAR(255),
    instagram VARCHAR(100),
    facebook VARCHAR(100),
    description VARCHAR(255),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS staff (
    id_staff BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_user BIGINT UNIQUE,            -- lien vers utilisateur
    id_cabinet BIGINT,
    salaire DOUBLE NOT NULL DEFAULT 0,
    prime DOUBLE DEFAULT 0,
    date_recrutement DATE,
    solde_conge INT DEFAULT 0,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_cabinet) REFERENCES cabinet_medical(id_cabinet) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS agenda_mensuel (
    id_agenda BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
	id_medecin BIGINT,         -- staff.id_staff
    mois ENUM('JANVIER','FEVRIER','MARS','AVRIL','MAI','JUIN','JUILLET','AOUT','SEPTEMBRE','OCTOBRE','NOVEMBRE','DECEMBRE'),
    jours_non_disponible TEXT, 
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL,
    FOREIGN KEY (id_medecin) REFERENCES staff(id_staff) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS medecin (
	id_medecin BIGINT PRIMARY KEY,
    id_staff BIGINT, -- FK vers staff
    specialite VARCHAR(100),
	jours_non_disponible TEXT, 
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_staff) REFERENCES staff(id_staff) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS secretaire (
	id_secretaire BIGINT PRIMARY KEY,
    id_staff BIGINT,
    num_cnss VARCHAR(50),
    commission DOUBLE DEFAULT 0,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_staff) REFERENCES staff(id_staff) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS patient (
    id_patient BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(100) NOT NULL,
    dateNaissance DATE,
    sexe ENUM('HOMME','FEMME','AUTRE'),
    adresse VARCHAR(255),
    telephone VARCHAR(20),
    assurance ENUM('CNOPS','CNSS','MUTUELLE','AUCUNE'),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    INDEX idx_patient_nom (nom),
    INDEX idx_patient_telephone (telephone),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS dossier_medical (
    id_dossier BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_patient BIGINT NOT NULL,
    date_de_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    INDEX idx_dossier_patient (id_patient),
    FOREIGN KEY (id_patient) REFERENCES patient(id_patient) ON DELETE CASCADE,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS situation_financiere (
    id_situation BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_dossier_medical BIGINT,
    totaleDesActes BIGINT UNIQUE, -- une situation par dossier
    totalePaye DOUBLE DEFAULT 0,
    credit DOUBLE DEFAULT 0,
    statut ENUM('SOLVABLE','IMPAYE','EN_RETARD','COMPTE_CLOS'),
    en_promo BOOLEAN DEFAULT FALSE,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_dossier_medical) REFERENCES dossier_medical(id_dossier) ON DELETE CASCADE,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS antecedent (
    id_antecedent BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(150) NOT NULL,
    categorie VARCHAR(100),
    niveauDeRisque ENUM('FAIBLE','MOYEN','ELEVE','CRITIQUE'),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

-- TABLE: patient_antecedent (many-to-many)
CREATE TABLE IF NOT EXISTS patient_antecedent (
    id_patient BIGINT,
    id_antecedent BIGINT,
    PRIMARY KEY (id_patient, id_antecedent),
    FOREIGN KEY (id_patient) REFERENCES patient(id_patient) ON DELETE CASCADE,
    FOREIGN KEY (id_antecedent) REFERENCES antecedent(id_antecedent) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS consultation (
    id_consultation BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_dossier_medical BIGINT,
    id_medecin BIGINT,    
    date_consultation DATE,
    heure_consultation TIME,
    statut ENUM('PLANIFIEE','EN_COURS','TERMINEE','ANNULEE'),
    observationMedecin TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_dossier_medical) REFERENCES dossier_medical(id_dossier) ON DELETE CASCADE,
    FOREIGN KEY (id_medecin) REFERENCES medecin(id_staff) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS rdv (
    id_rdv BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_dossier_medical BIGINT,
    id_consultation BIGINT,
    date_rdv DATE,
    heure TIME,
    motif VARCHAR(255),
    statut ENUM('PLANIFIE','CONFIRME','EN_ATTENTE','ANNULE','TERMINE'),
    noteMedecin TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_dossier_medical) REFERENCES dossier_medical(id_dossier) ON DELETE CASCADE,
    FOREIGN KEY (id_consultation) REFERENCES consultation(id_consultation) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS certificat (
    id_certificat BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_dossier_medical BIGINT,
    id_consultation BIGINT UNIQUE, -- 1 certificat par une seule consultation
    dateDebut DATE,
    dateFin DATE,
    duree INT,
    noteMedecin TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_dossier_medical) REFERENCES dossier_medical(id_dossier) ON DELETE CASCADE,
    FOREIGN KEY (id_consultation) REFERENCES consultation(id_consultation) ON DELETE CASCADE,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ordonnance (
    id_ordonnance BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    date_ord DATE,
    note TEXT,
    id_dossier_medical BIGINT,
    id_consultation BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_dossier_medical) REFERENCES dossier_medical(id_dossier) ON DELETE CASCADE,
    FOREIGN KEY (id_consultation) REFERENCES consultation(id_consultation) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS medicament (
    id_medicament BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(150) NOT NULL,
    laboratoire VARCHAR(100),
    type_medicament VARCHAR(100),
    forme ENUM('COMPRIME','CAPSULE','SIROP','INJECTION','POMMADE','AUTRE'),
    remboursable BOOLEAN DEFAULT FALSE,
    prix_unitaire DOUBLE,
    `description` TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    INDEX idx_medicament_nom (nom),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS prescription (
    id_prescription BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    quantite INT,
    frequence VARCHAR(100),
    duree_en_jours INT,
    id_ordonnance BIGINT,
    id_medicament BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_ordonnance) REFERENCES ordonnance(id_ordonnance) ON DELETE CASCADE,
    FOREIGN KEY (id_medicament) REFERENCES medicament(id_medicament) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS acte (
    id_acte BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    libelle VARCHAR(150) NOT NULL,
    categorie VARCHAR(100),
    prix_de_base DOUBLE,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    INDEX idx_acte_libelle (libelle),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS intervention_medecin (
    id_intervention BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    prix_de_patient DOUBLE,
    num_dent INT,
    id_consultation BIGINT,
    id_acte BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_consultation) REFERENCES consultation(id_consultation) ON DELETE CASCADE,
    FOREIGN KEY (id_acte) REFERENCES acte(id_acte) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS facture (
    id_facture BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    totale_facture DOUBLE DEFAULT 0,
    totale_paye DOUBLE DEFAULT 0,
    reste DOUBLE AS (totale_facture - totale_paye) STORED,
    statut ENUM('PAYEE','PARTIELLEMENT_PAYEE','IMPAYEE','ANNULEE'),
    date_facture DATETIME,
    id_situation_financiere BIGINT,
    id_consultation BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_situation_financiere) REFERENCES situation_financiere(id_situation) ON DELETE SET NULL,
    FOREIGN KEY (id_consultation) REFERENCES consultation(id_consultation) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS caisse (
    id_caisse BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    id_facture BIGINT,
    montant DOUBLE NOT NULL,
    date_encaissement DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mode_encaissement ENUM('ESPECES','CARTE_BANCAIRE','CHEQUE','VIREMENT','ASSURANCE'),
    `reference` VARCHAR(100),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cree_par VARCHAR(100),
    FOREIGN KEY (id_facture) REFERENCES facture(id_facture) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS charges (
    id_charge BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    titre VARCHAR(150),
    `description` TEXT,
    montant DOUBLE,
    date_charge DATETIME,
    id_cabinet BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_cabinet) REFERENCES cabinet_medical(id_cabinet) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS revenues (
    id_revenue BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    titre VARCHAR(150),
    `description` TEXT,
    montant DOUBLE,
    date_revenue DATETIME,
    id_cabinet BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_cabinet) REFERENCES cabinet_medical(id_cabinet) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS statistique (
    id_statistique BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    nom VARCHAR(150),
    categorie ENUM('FINANCIERE','PATIENT','CONSULTATION','PERFORMANCE'),
    chiffre DOUBLE,
    date_calcul DATE,
    id_cabinet BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_cabinet) REFERENCES cabinet_medical(id_cabinet) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS file_attente (
    id_file_attente BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    date_file DATE,
    capacite INT,
    est_ouverte BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS role (
    id_role BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    libelle ENUM('ADMIN','MEDECIN','SECRETAIRE','PATIENT'),
	`privileges` TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

-- TABLE: utilisateur_role (many-to-many)
CREATE TABLE IF NOT EXISTS utilisateur_role (
    id_user BIGINT,
    id_role BIGINT,
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_role) REFERENCES role(id_role) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notification (
    id_notif BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    titre VARCHAR(255),
    message TEXT,
    date_notif DATE,
    heure TIME,
    type_notif ENUM('RAPPEL','ALERTE','INFO','URGENCE'),
    priorite ENUM('BASSE','MOYENNE','HAUTE','CRITIQUE'),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);

-- TABLE: utilisateur_notification (many-to-many)
CREATE TABLE IF NOT EXISTS utilisateur_notification (
    id_user BIGINT,
    id_notif BIGINT,
    lu BOOLEAN DEFAULT FALSE,
    date_lu DATETIME,
    PRIMARY KEY (id_user, id_notif),
    FOREIGN KEY (id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_notif) REFERENCES notification(id_notif) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `log` (
    id_log BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_entite BIGINT,
    `action` VARCHAR(255),
    date_heure DATETIME,
    ip_adresse VARCHAR(45),
    statut VARCHAR(50),
    id_utilisateur BIGINT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modifie_par VARCHAR(100),
    cree_par VARCHAR(100),
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_user) ON DELETE SET NULL,
    FOREIGN KEY (id_entite) REFERENCES BaseEntity(id_entite) ON DELETE SET NULL
);
