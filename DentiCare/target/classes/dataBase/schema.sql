CREATE TABLE IF NOT EXISTS CabinetMedicale (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(120),
    logo VARCHAR(255),
    rue VARCHAR(150),
    ville VARCHAR(100),
    codePostal VARCHAR(20),
    region VARCHAR(100),
    pays VARCHAR(100),
    cin VARCHAR(50),
    tel1 VARCHAR(30),
    tel2 VARCHAR(30),
    siteWeb VARCHAR(255),
    instagram VARCHAR(255),
    facebook VARCHAR(255),
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    UNIQUE KEY uk_cabinet_email (email)
);

CREATE TABLE IF NOT EXISTS Utilisateur (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL,
    adresse VARCHAR(150),
    cin VARCHAR(50),
    tel VARCHAR(30),
    sexe ENUM('MASCULIN', 'FEMININ') NOT NULL,
    login VARCHAR(50) NOT NULL,
    motDePass VARCHAR(255) NOT NULL,
    lastLoginDate DATE,
    dateNaissance DATE,
    cabinetMedicale_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    UNIQUE KEY uk_utilisateur_email (email),
    UNIQUE KEY uk_utilisateur_login (login),
    FOREIGN KEY (cabinetMedicale_id) REFERENCES CabinetMedicale(idEntite) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Role (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    libelle ENUM('ADMIN', 'MEDECIN', 'SECRETAIRE') NOT NULL,
    privileges JSON,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Utilisateur_Role (
    utilisateur_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (utilisateur_id, role_id),
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Role(idEntite) ON DELETE CASCADE
); -- (Many-to-Many)

CREATE TABLE IF NOT EXISTS Staff (
    idEntite BIGINT PRIMARY KEY,
    salaire DECIMAL(10,2) NOT NULL,
    prime DECIMAL(10,2) DEFAULT 0,
    dateRecrutement DATE NOT NULL,
    soldeConge INT DEFAULT 0,
    FOREIGN KEY (idEntite) REFERENCES Utilisateur(idEntite) ON DELETE CASCADE
);

-- Création de AgendaMensuel d'abord (sans FK vers Medecin pour éviter référence circulaire)
CREATE TABLE IF NOT EXISTS AgendaMensuel (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    mois ENUM('JANVIER', 'FEVRIER', 'MARS', 'AVRIL', 'MAI', 'JUIN', 
              'JUILLET', 'AOUT', 'SEPTEMBRE', 'OCTOBRE', 'NOVEMBRE', 'DECEMBRE') NOT NULL,
    joursNonDisponible JSON,
    medecin_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    UNIQUE KEY uk_agenda_medecin (medecin_id)
);

CREATE TABLE IF NOT EXISTS Medecin (
    idEntite BIGINT PRIMARY KEY,
    specialite VARCHAR(100),
    agendaMensuel_id BIGINT,
    FOREIGN KEY (idEntite) REFERENCES Staff(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (agendaMensuel_id) REFERENCES AgendaMensuel(idEntite) ON DELETE SET NULL
);

-- Ajout de la contrainte de clé étrangère pour medecin_id dans AgendaMensuel
ALTER TABLE AgendaMensuel ADD FOREIGN KEY (medecin_id) REFERENCES Medecin(idEntite) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS Secretaire (
    idEntite BIGINT PRIMARY KEY,
    numCNSS VARCHAR(50),
    commission DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (idEntite) REFERENCES Staff(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Admin (
    idEntite BIGINT PRIMARY KEY,
    FOREIGN KEY (idEntite) REFERENCES Utilisateur(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DossierMedicale (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    dateDeCreation DATE NOT NULL,
    patient_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS SituationFinanciere (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    totaleDesActes DECIMAL(10,2) DEFAULT 0,
    totalePaye DECIMAL(10,2) DEFAULT 0,
    credit DECIMAL(10,2) DEFAULT 0,
    statut ENUM('SOLVABLE', 'IMPAYE', 'EN_RETARD', 'COMPTE_CLOS') NOT NULL,
    enPromo BOOLEAN DEFAULT FALSE,
    patient_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Patients (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(80) NOT NULL,
    dateDeNaissance DATE,
    sexe ENUM('MASCULIN', 'FEMININ') NOT NULL,
    adresse VARCHAR(150),
    telephone VARCHAR(30),
    assurance ENUM('CNSS', 'MUTUELLE_PRIVEE', 'AUCUNE') NOT NULL,
    dossierMedicale_id BIGINT,
    situationFinanciere_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    UNIQUE KEY uk_patients_dossier (dossierMedicale_id),
    UNIQUE KEY uk_patients_situation (situationFinanciere_id),
    FOREIGN KEY (dossierMedicale_id) REFERENCES DossierMedicale(idEntite) ON DELETE SET NULL,
    FOREIGN KEY (situationFinanciere_id) REFERENCES SituationFinanciere(idEntite) ON DELETE SET NULL
);

-- Contraintes de clés étrangères pour les relations 1:1 inverses
ALTER TABLE DossierMedicale ADD FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE;
ALTER TABLE SituationFinanciere ADD FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE;
ALTER TABLE DossierMedicale ADD UNIQUE KEY uk_dossier_patient (patient_id);
ALTER TABLE SituationFinanciere ADD UNIQUE KEY uk_situation_patient (patient_id);

CREATE TABLE IF NOT EXISTS Antecedents (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50),
    niveauDeRisque ENUM('FAIBLE', 'MODERE', 'ELEVE', 'CRITIQUE') NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Patient_Antecedents (
    patient_id BIGINT NOT NULL,
    antecedent_id BIGINT NOT NULL,
    PRIMARY KEY (patient_id, antecedent_id),
    FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (antecedent_id) REFERENCES Antecedents(idEntite) ON DELETE CASCADE
); --(Many-to-Many)


CREATE TABLE IF NOT EXISTS Consultation (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    statut ENUM('OUVERTE', 'EN_COURS', 'TERMINEE', 'FACTURABLE') NOT NULL,
    observationMedecin TEXT,
    dossierMedicale_id BIGINT NOT NULL,
    medecin_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (dossierMedicale_id) REFERENCES DossierMedicale(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES Medecin(idEntite) ON DELETE CASCADE,
    KEY idx_consultation_date (`date`)
);

CREATE TABLE IF NOT EXISTS Ordonnance (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    consultation_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (consultation_id) REFERENCES Consultation(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Medicament (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    laboratoire VARCHAR(100),
    `type` VARCHAR(50),
    forme ENUM('COMPRIME', 'CAPSULE', 'SIROP', 'POMMADE', 'INJECTION') NOT NULL,
    remboursable BOOLEAN DEFAULT FALSE,
    prixUnitaire DECIMAL(10,2),
    `description` TEXT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Prescription (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantite INT NOT NULL,
    frequence VARCHAR(50),
    dureeEnJours INT,
    ordonnance_id BIGINT NOT NULL,
    medicament_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (ordonnance_id) REFERENCES Ordonnance(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (medicament_id) REFERENCES Medicament(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Acte (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(150) NOT NULL,
    categorie VARCHAR(50),
    prixDeBase DECIMAL(10,2) NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS InterventionMedecin (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    prixDePatient DECIMAL(10,2) NOT NULL,
    numDent INT,
    consultation_id BIGINT NOT NULL,
    acte_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (consultation_id) REFERENCES Consultation(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (acte_id) REFERENCES Acte(idEntite) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Facture (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    totaleFacture DECIMAL(10,2) NOT NULL,
    totalePaye DECIMAL(10,2) DEFAULT 0,
    reste DECIMAL(10,2) NOT NULL,
    statut ENUM('EN_ATTENTE', 'PAYEE_PARTIELLEMENT', 'PAYEE_INTEGRALEMENT', 'ANNULEE') NOT NULL,
    dateFacture DATETIME NOT NULL,
    patient_id BIGINT NOT NULL,
    situationFinanciere_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (situationFinanciere_id) REFERENCES SituationFinanciere(idEntite) ON DELETE CASCADE,
    KEY idx_facture_date (dateFacture)
);

CREATE TABLE IF NOT EXISTS RDV (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    heure TIME NOT NULL,
    motif VARCHAR(255),
    statut ENUM('PLANIFIE', 'CONFIRME', 'ANNULE', 'TERMINE') NOT NULL,
    noteMedecin TEXT,
    patient_id BIGINT NOT NULL,
    medecin_id BIGINT NOT NULL,
    fileAttente_id BIGINT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES Medecin(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (fileAttente_id) REFERENCES FileAttente(idEntite) ON DELETE SET NULL,
    KEY idx_rdv_date (`date`)
);

CREATE TABLE IF NOT EXISTS FileAttente (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    capacite INT NOT NULL,
    estOuverte BOOLEAN DEFAULT TRUE,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    KEY idx_fileattente_date (`date`)
);

CREATE TABLE IF NOT EXISTS Certificat (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    dateDebut DATE NOT NULL,
    dateFin DATE NOT NULL,
    duree INT NOT NULL,
    noteMedecin TEXT,
    patient_id BIGINT NOT NULL,
    medecin_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (patient_id) REFERENCES Patients(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES Medecin(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Notification (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre ENUM('RENDEZ_VOUS', 'ALERTE_SYSTEME', 'RAPPORT_FINANCIER', 'PATIENT_NOUVEAU') NOT NULL,
    `message` TEXT NOT NULL,
    `date` DATE NOT NULL,
    `time` TIME NOT NULL,
    `type` ENUM('RENDEZ_VOUS', 'ALERTE_SYSTEME', 'RAPPORT_FINANCIER', 'PATIENT_NOUVEAU') NOT NULL,
    priorite ENUM('BASSE', 'NORMALE', 'URGENTE', 'CRITIQUE') NOT NULL,
    `description` TEXT,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    KEY idx_notification_date (`date`)
);

CREATE TABLE IF NOT EXISTS Utilisateur_Notification (
    utilisateur_id BIGINT NOT NULL,
    notification_id BIGINT NOT NULL,
    PRIMARY KEY (utilisateur_id, notification_id),
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(idEntite) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES Notification(idEntite) ON DELETE CASCADE
);--(Many-to-Many)

CREATE TABLE IF NOT EXISTS Log (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    `action` VARCHAR(255) NOT NULL,
    dateHeure DATETIME NOT NULL,
    ipAdresse VARCHAR(45),
    `status` VARCHAR(50),
    utilisateur_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(idEntite) ON DELETE CASCADE,
    KEY idx_log_date (dateHeure),
    KEY idx_log_user (utilisateur_id)
);

CREATE TABLE IF NOT EXISTS Statistique (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    totalPatient INT DEFAULT 0,
    totalConsultation INT DEFAULT 0,
    totalAnnulations INT DEFAULT 0,
    revenueMensuel DECIMAL(10,2) DEFAULT 0,
    revenueAnnuel DECIMAL(10,2) DEFAULT 0,
    cabinetMedicale_id BIGINT NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    FOREIGN KEY (cabinetMedicale_id) REFERENCES CabinetMedicale(idEntite) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Charges (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(150) NOT NULL,
    `description` TEXT,
    montant DECIMAL(10,2) NOT NULL,
    `date` DATETIME NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    KEY idx_charges_date (`date`)
);

CREATE TABLE IF NOT EXISTS Revenues (
    idEntite BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(150) NOT NULL,
    `description` TEXT,
    montant DECIMAL(10,2) NOT NULL,
    `date` DATETIME NOT NULL,
    dateCreation DATE NOT NULL DEFAULT (CURRENT_DATE),
    dateDerniereModification DATETIME,
    modifiePar VARCHAR(100),
    creePar VARCHAR(100),
    KEY idx_revenues_date (`date`)
);
