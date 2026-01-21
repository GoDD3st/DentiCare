-- Réinitialisation des AUTO_INCREMENT
ALTER TABLE BaseEntity AUTO_INCREMENT = 1;
ALTER TABLE utilisateur AUTO_INCREMENT = 1;
ALTER TABLE cabinet_medical AUTO_INCREMENT = 1;
ALTER TABLE staff AUTO_INCREMENT = 1;
ALTER TABLE patient AUTO_INCREMENT = 1;
ALTER TABLE dossier_medical AUTO_INCREMENT = 1;
ALTER TABLE situation_financiere AUTO_INCREMENT = 1;
ALTER TABLE antecedent AUTO_INCREMENT = 1;
ALTER TABLE consultation AUTO_INCREMENT = 1;
ALTER TABLE rdv AUTO_INCREMENT = 1;
ALTER TABLE ordonnance AUTO_INCREMENT = 1;
ALTER TABLE medicament AUTO_INCREMENT = 1;
ALTER TABLE prescription AUTO_INCREMENT = 1;
ALTER TABLE acte AUTO_INCREMENT = 1;
ALTER TABLE intervention_medecin AUTO_INCREMENT = 1;
ALTER TABLE facture AUTO_INCREMENT = 1;
ALTER TABLE caisse AUTO_INCREMENT = 1;
ALTER TABLE file_attente AUTO_INCREMENT = 1;
ALTER TABLE certificat AUTO_INCREMENT = 1;
ALTER TABLE notification AUTO_INCREMENT = 1;
ALTER TABLE `log` AUTO_INCREMENT = 1;
ALTER TABLE statistique AUTO_INCREMENT = 1;
ALTER TABLE charges AUTO_INCREMENT = 1;
ALTER TABLE revenues AUTO_INCREMENT = 1;
ALTER TABLE role AUTO_INCREMENT = 1;
ALTER TABLE agenda_mensuel AUTO_INCREMENT = 1;

-- 1. BaseEntity (table de base pour toutes les entités)
INSERT INTO BaseEntity (cree_par) VALUES
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'),
('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system'), ('system');

-- 2. CabinetMedicale (pas de dépendances)
INSERT INTO cabinet_medical (id_entite, nom, email, logo, adresse, tel1, tel2, siteWeb, instagram, facebook, description, cree_par) VALUES
(1, 'Cabinet Dentaire Alami', 'contact@cabinet-alami.ma', 'logo.png', 'Avenue Hassan II, Rabat', '0537771234', '0537771235', 'www.cabinet-alami.ma', '@cabinet_alami', 'CabinetDentaireAlami', 'Cabinet dentaire moderne et équipé', 'admin');

-- 3. Utilisateur (dépend de BaseEntity)
-- id_user AUTO_INCREMENT: 1=Marouane, 2=Dr.Alami, 3=Dr.Benani, 4=Salma
INSERT INTO utilisateur (id_entite, nom, email, adresse, cin, tel, sexe, login, motDePass, lastLoginDate, dateNaissance, cree_par) VALUES
(2, 'Marouane EL Mrhari', 'admin@dentaltech.ma', 'Rabat, Agdal', 'AB123456', '0612345678', 'HOMME', 'admin', 'admin', '2025-12-23 08:30:00', '2000-05-15', 'system'),
(3, 'Dr. Ahmed Alami', 'ahmed.alami@cabinet.ma', 'Rabat, Hay Riad', 'CD789012', '0623456789', 'HOMME', 'dr.alami', 'alami', '2025-12-22 09:00:00', '1985-03-20', 'admin'),
(4, 'Dr. Fatima Benani', 'fatima.benani@cabinet.ma', 'Rabat, Agdal', 'EF345678', '0634567890', 'FEMME', 'dr.benani', 'benani', '2025-12-21 10:00:00', '1990-07-10', 'admin'),
(5, 'Salma El Fassi', 'salma.elfassi@cabinet.ma', 'Rabat, Centre', 'GH901234', '0645678901', 'FEMME', 'salma.sec', 'salma', '2025-12-20 08:00:00', '1995-11-25', 'admin');

-- 4. Admin (dépend de Utilisateur)
-- id_user=1 (Marouane)
INSERT INTO `admin` (id_user) VALUES (1);

-- 5. Staff (dépend de Utilisateur et CabinetMedicale)
-- id_staff AUTO_INCREMENT: 1=Dr.Alami, 2=Dr.Benani, 3=Salma
INSERT INTO staff (id_user, id_cabinet, salaire, prime, date_recrutement, solde_conge, cree_par) VALUES
(2, 1, 15000.00, 2000.00, '2020-01-15', 15, 'admin'), -- Dr. Alami (id_user=2, id_staff=1)
(3, 1, 14500.00, 1500.00, '2021-03-01', 12, 'admin'), -- Dr. Benani (id_user=3, id_staff=2)
(4, 1, 8000.00, 500.00, '2022-06-10', 20, 'admin');  -- Salma (id_user=4, id_staff=3)

-- 6. Medecin (dépend de Staff)
-- id_staff: 1=Dr.Alami, 2=Dr.Benani
INSERT INTO medecin (id_medecin, id_staff, specialite, jours_non_disponible, cree_par) VALUES
(1, 1, 'Orthodontie', '["SAMEDI", "DIMANCHE"]', 'admin'), -- Dr. Alami (id_staff=1)
(2, 2, 'Chirurgie dentaire', '["DIMANCHE"]', 'admin'); -- Dr. Benani (id_staff=2)

-- 7. Secretaire (dépend de Staff)
-- id_staff: 3=Salma
INSERT INTO secretaire (id_secretaire, id_staff, num_cnss, commission, cree_par) VALUES
(1, 3, 'J123456789', 2.5, 'admin');

-- 8. AgendaMensuel (dépend de Medecin via staff)
-- id_medecin référence staff.id_staff (1=Dr.Alami, 2=Dr.Benani)
INSERT INTO agenda_mensuel (id_entite, id_medecin, mois, jours_non_disponible, cree_par) VALUES
(6, 1, 'DECEMBRE', '["SAMEDI", "DIMANCHE"]', 'admin'), -- Dr. Alami (id_staff=1)
(7, 2, 'DECEMBRE', '["DIMANCHE"]', 'admin'); -- Dr. Benani (id_staff=2)

-- 9. Role
-- id_role AUTO_INCREMENT: 1=ADMIN, 2=MEDECIN, 3=SECRETAIRE
INSERT INTO role (id_entite, libelle, `privileges`, cree_par) VALUES
(8, 'ADMIN', '["gestion_complete", "gestion_utilisateurs", "rapports"]', 'system'),
(9, 'MEDECIN', '["consultation", "ordonnance", "certificat", "dossier_medical"]', 'system'),
(10, 'SECRETAIRE', '["rdv", "facturation", "patients"]', 'system');

-- 10. Utilisateur_Role
-- id_user: 1=Marouane, 2=Dr.Alami, 3=Dr.Benani, 4=Salma
-- id_role: 1=ADMIN, 2=MEDECIN, 3=SECRETAIRE
INSERT INTO utilisateur_role (id_user, id_role) VALUES
(1, 1), -- Marouane -> ADMIN
(2, 2), -- Dr. Alami -> MEDECIN
(3, 2), -- Dr. Benani -> MEDECIN
(4, 3); -- Salma -> SECRETAIRE

-- 11. Patient (dépend de BaseEntity)
-- id_patient AUTO_INCREMENT: 1=Amal, 2=Omar, 3=Nour, 4=Youssef, 5=Hiba, 6=Mahdi
INSERT INTO patient (id_entite, nom, dateNaissance, sexe, adresse, telephone, assurance, cree_par) VALUES
(11, 'Amal Zahra', '1995-05-12', 'FEMME', 'Rabat, Hay Riad', '0611111111', 'CNSS', 'admin'),
(12, 'Omar Badr', '1989-09-23', 'HOMME', 'Salé, Centre', '0622222222', 'MUTUELLE', 'admin'),
(13, 'Nour Chafi', '2000-02-02', 'FEMME', 'Témara', '0633333333', 'CNSS', 'admin'),
(14, 'Youssef Dari', '1992-11-01', 'HOMME', 'Kénitra', '0644444444', 'AUCUNE', 'admin'),
(15, 'Hiba Zerouali', '2001-03-14', 'FEMME', 'Rabat, Agdal', '0655555555', 'CNSS', 'admin'),
(16, 'Mahdi daoui', '1990-07-18', 'HOMME', 'Casablanca', '0666666666', 'MUTUELLE', 'admin');

-- 12. DossierMedicale (dépend de Patient)
-- id_dossier AUTO_INCREMENT: 1-6
-- id_patient: 1=Amal, 2=Omar, 3=Nour, 4=Youssef, 5=Hiba, 6=Mahdi
INSERT INTO dossier_medical (id_entite, id_patient, date_de_creation, cree_par) VALUES
(17, 1, '2025-10-25', 'admin'), -- Amal Zahra
(18, 2, '2025-10-25', 'admin'), -- Omar Badr
(19, 3, '2025-10-24', 'admin'), -- Nour Chafi
(20, 4, '2025-10-23', 'admin'), -- Youssef Dari
(21, 5, '2025-10-26', 'admin'), -- Hiba Zerouali
(22, 6, '2025-10-26', 'admin'); -- Mahdi daoui

-- 13. SituationFinanciere (dépend de DossierMedicale)
-- id_situation AUTO_INCREMENT: 1-6
-- id_dossier_medical: 1-6
INSERT INTO situation_financiere (id_entite, id_dossier_medical, totaleDesActes, totalePaye, credit, statut, en_promo, cree_par) VALUES
(23, 1, 1500, 1000.00, 500.00, 'EN_RETARD', FALSE, 'admin'),
(24, 2, 2000, 2000.00, 0.00, 'SOLVABLE', FALSE, 'admin'),
(25, 3, 800, 500.00, 300.00, 'EN_RETARD', FALSE, 'admin'),
(26, 4, 1200, 0.00, 1200.00, 'IMPAYE', FALSE, 'admin'),
(27, 5, 500, 500.00, 0.00, 'SOLVABLE', FALSE, 'admin'),
(28, 6, 3000, 2000.00, 1000.00, 'EN_RETARD', FALSE, 'admin');

-- 14. Antecedents (dépend de BaseEntity)
-- id_antecedent AUTO_INCREMENT: 1-12
INSERT INTO antecedent (id_entite, nom, categorie, niveauDeRisque, cree_par) VALUES
(29, 'Allergie à la pénicilline', 'ALLERGIE', 'CRITIQUE', 'admin'),
(30, 'Allergie au latex', 'ALLERGIE', 'ELEVE', 'admin'),
(31, 'Allergie aux anesthésiques locaux', 'ALLERGIE', 'CRITIQUE', 'admin'),
(32, 'Diabète de type 2', 'MALADIE_CHRONIQUE', 'MOYEN', 'admin'),
(33, 'Hypertension artérielle', 'MALADIE_CHRONIQUE', 'MOYEN', 'admin'),
(34, 'Asthme', 'MALADIE_CHRONIQUE', 'ELEVE', 'admin'),
(35, 'Sous traitement anticoagulant', 'CONTRE_INDICATION', 'ELEVE', 'admin'),
(36, 'Grossesse', 'CONTRE_INDICATION', 'MOYEN', 'admin'),
(37, 'Prothèse valvulaire cardiaque', 'ANTECEDENT_CHIRURGICAL', 'ELEVE', 'admin'),
(38, 'Hépatite B ancienne', 'ANTECEDENT_INFECTIEUX', 'MOYEN', 'admin'),
(39, 'Tabagisme chronique', 'HABITUDE_DE_VIE', 'MOYEN', 'admin'),
(40, 'Alcoolisme', 'HABITUDE_DE_VIE', 'ELEVE', 'admin');

-- 15. Patient_Antecedents
-- id_patient: 1=Amal, 2=Omar, 3=Nour, 4=Youssef
-- id_antecedent: 1=Pénicilline, 2=Latex, 3=Anesthésiques, 4=Diabète, 5=Hypertension, 6=Asthme, 7=Anticoagulant, 8=Grossesse, 9=Prothèse, 10=Hépatite, 11=Tabagisme, 12=Alcoolisme
INSERT INTO patient_antecedent (id_patient, id_antecedent) VALUES
(1, 2), (1, 4), -- Amal: allergie latex (2), diabète (4)
(2, 5), (2, 11), (2, 2), -- Omar: hypertension (5), tabagisme (11), allergie latex (2)
(3, 1), (3, 8), -- Nour: allergie pénicilline (1), grossesse (8)
(4, 9); -- Youssef: prothèse valvulaire (9)

-- 16. Consultation (dépend de DossierMedicale et Medecin)
-- id_consultation AUTO_INCREMENT: 1-6
-- id_dossier_medical: 1-6
-- id_medecin référence medecin.id_staff (1=Dr.Alami, 2=Dr.Benani)
INSERT INTO consultation (id_entite, id_dossier_medical, id_medecin, date_consultation, heure_consultation, statut, observationMedecin, cree_par) VALUES
(41, 1, 1, '2025-12-20', '09:00:00', 'TERMINEE', 'Patient présente des douleurs dentaires. Carie détectée sur la molaire 36.', 'dr.alami'), -- Dr. Alami (id_staff=1)
(42, 2, 1, '2025-12-21', '10:30:00', 'EN_COURS', 'Consultation de contrôle post-traitement.', 'dr.alami'), -- Dr. Alami (id_staff=1)
(43, 3, 2, '2025-12-22', '14:00:00', 'TERMINEE', 'Détartrage complet effectué. Bilan de santé bucco-dentaire.', 'dr.benani'), -- Dr. Benani (id_staff=2)
(44, 4, 1, '2025-12-23', '11:00:00', 'PLANIFIEE', 'Première consultation. Examen complet à effectuer.', 'dr.alami'), -- Dr. Alami (id_staff=1)
(45, 5, 1, '2025-12-18', '15:30:00', 'TERMINEE', 'Traitement orthodontique - ajustement des bagues.', 'dr.alami'), -- Dr. Alami (id_staff=1)
(46, 6, 2, '2025-12-19', '16:00:00', 'TERMINEE', 'Extraction dent de sagesse. Suivi post-opératoire nécessaire.', 'dr.benani'); -- Dr. Benani (id_staff=2)

-- 17. RDV (dépend de DossierMedicale et Consultation)
-- id_rdv AUTO_INCREMENT: 1-7
-- id_dossier_medical: 1-5 (ou NULL)
-- id_consultation: 1-5 (ou NULL)
INSERT INTO rdv (id_entite, id_dossier_medical, id_consultation, date_rdv, heure, motif, statut, noteMedecin, cree_par) VALUES
(47, 1, 1, '2025-12-20', '09:00:00', 'Consultation de contrôle', 'TERMINE', NULL, 'salma.sec'),
(48, 2, 2, '2025-12-21', '10:30:00', 'Détartrage', 'TERMINE', NULL, 'salma.sec'),
(49, 3, 3, '2025-12-22', '14:00:00', 'Extraction dent', 'TERMINE', NULL, 'salma.sec'),
(50, 4, 4, '2025-12-23', '11:00:00', 'Consultation urgente', 'CONFIRME', 'Douleur aiguë', 'salma.sec'),
(51, 5, 5, '2025-12-18', '15:30:00', 'Suivi orthodontie', 'TERMINE', NULL, 'salma.sec'),
(52, NULL, NULL, '2025-12-24', '09:00:00', 'Consultation de contrôle', 'PLANIFIE', NULL, 'salma.sec'),
(53, NULL, NULL, '2025-12-25', '14:00:00', 'Extraction dent', 'PLANIFIE', NULL, 'salma.sec');

-- 18. Ordonnance (dépend de DossierMedicale et Consultation)
-- id_ordonnance AUTO_INCREMENT: 1-3
-- id_dossier_medical: 1, 3, 6
-- id_consultation: 1, 3, 6
INSERT INTO ordonnance (id_entite, date_ord, note, id_dossier_medical, id_consultation, cree_par) VALUES
(54, '2025-12-20', 'Traitement antibiotique et antalgique', 1, 1, 'dr.alami'),
(55, '2025-12-22', 'Bain de bouche antiseptique', 3, 3, 'dr.benani'),
(56, '2025-12-19', 'Antalgiques post-opératoires', 6, 6, 'dr.benani');

-- 19. Medicament (dépend de BaseEntity)
-- id_medicament AUTO_INCREMENT: 1-5
INSERT INTO medicament (id_entite, nom, laboratoire, type_medicament, forme, remboursable, prix_unitaire, `description`, cree_par) VALUES
(57, 'Paracétamol 500mg', 'Sanofi', 'Antalgique', 'COMPRIME', TRUE, 12.50, 'Pour douleur et fièvre', 'admin'),
(58, 'Amoxicilline 1g', 'Biopharm', 'Antibiotique', 'COMPRIME', TRUE, 45.00, 'Traitement des infections', 'admin'),
(59, 'Ibuprofène 400mg', 'Pfizer', 'Anti-inflammatoire', 'COMPRIME', TRUE, 18.75, 'Douleur et inflammation', 'admin'),
(60, 'Bain de bouche Chlorhexidine', 'Pierre Fabre', 'Antiseptique', 'SIROP', FALSE, 35.00, 'Hygiène bucco-dentaire', 'admin'),
(61, 'Pommade anesthésiante', 'Laboratoires Innotech', 'Anesthésique local', 'POMMADE', FALSE, 28.50, 'Application locale', 'admin');

-- 20. Prescription (dépend de Ordonnance et Medicament)
-- id_prescription AUTO_INCREMENT: 1-5
-- id_ordonnance: 1-3
-- id_medicament: 1-5
INSERT INTO prescription (id_entite, quantite, frequence, duree_en_jours, id_ordonnance, id_medicament, cree_par) VALUES
(62, 20, '3 fois par jour', 7, 1, 1, 'dr.alami'), -- Ordonnance 1, Paracétamol
(63, 14, '2 fois par jour', 7, 1, 2, 'dr.alami'), -- Ordonnance 1, Amoxicilline
(64, 15, '2 fois par jour après repas', 7, 2, 4, 'dr.benani'), -- Ordonnance 2, Bain de bouche
(65, 10, '3 fois par jour', 5, 3, 1, 'dr.benani'), -- Ordonnance 3, Paracétamol
(66, 14, '2 fois par jour', 7, 3, 2, 'dr.benani'); -- Ordonnance 3, Amoxicilline

-- 21. Acte (dépend de BaseEntity)
-- id_acte AUTO_INCREMENT: 1-8
INSERT INTO acte (id_entite, libelle, categorie, prix_de_base, cree_par) VALUES
(67, 'Détartrage complet', 'Soins préventifs', 300.00, 'admin'),
(68, 'Soin de carie', 'Soins curatifs', 500.00, 'admin'),
(69, 'Extraction dent de sagesse', 'Chirurgie', 800.00, 'admin'),
(70, 'Traitement de canal', 'Endodontie', 1200.00, 'admin'),
(71, 'Pose de couronne', 'Prothèse', 2000.00, 'admin'),
(72, 'Blanchiment dentaire', 'Esthétique', 1500.00, 'admin'),
(73, 'Consultation de contrôle', 'Consultation', 150.00, 'admin'),
(74, 'Ajustement orthodontique', 'Orthodontie', 400.00, 'admin');

-- 22. InterventionMedecin (dépend de Consultation et Acte)
-- id_intervention AUTO_INCREMENT: 1-5
-- id_consultation: 1, 2, 3, 5, 6
-- id_acte: 1-8
INSERT INTO intervention_medecin (id_entite, prix_de_patient, num_dent, id_consultation, id_acte, cree_par) VALUES
(75, 500.00, 36, 1, 2, 'dr.alami'), -- Consultation 1, Soin de carie (acte 2)
(76, 300.00, NULL, 3, 1, 'dr.benani'), -- Consultation 3, Détartrage (acte 1)
(77, 800.00, 48, 6, 3, 'dr.benani'), -- Consultation 6, Extraction (acte 3)
(78, 400.00, NULL, 5, 8, 'dr.alami'), -- Consultation 5, Ajustement orthodontique (acte 8)
(79, 150.00, NULL, 2, 7, 'dr.alami'); -- Consultation 2, Consultation contrôle (acte 7)

-- 23. Facture (dépend de SituationFinanciere et Consultation)
-- id_facture AUTO_INCREMENT: 1-6
-- id_situation_financiere: 1-6
-- id_consultation: 1, 3, 4, 5, 6 (ou NULL)
INSERT INTO facture (id_entite, totale_facture, totale_paye, statut, date_facture, id_situation_financiere, id_consultation, cree_par) VALUES
(80, 500.00, 300.00, 'PARTIELLEMENT_PAYEE', '2025-12-20 14:30:00', 1, 1, 'salma.sec'),
(81, 2000.00, 2000.00, 'PAYEE', '2025-12-22 10:15:00', 2, NULL, 'salma.sec'),
(82, 300.00, 200.00, 'PARTIELLEMENT_PAYEE', '2025-12-22 16:45:00', 3, 3, 'salma.sec'),
(83, 1200.00, 0.00, 'IMPAYEE', '2025-12-23 09:00:00', 4, 4, 'salma.sec'),
(84, 400.00, 400.00, 'PAYEE', '2025-12-18 11:20:00', 5, 5, 'salma.sec'),
(85, 800.00, 500.00, 'PARTIELLEMENT_PAYEE', '2025-12-19 15:30:00', 6, 6, 'salma.sec');

-- 24. Caisse (dépend de Facture)
-- id_caisse AUTO_INCREMENT: 1-5
-- id_facture: 1, 2, 3, 5, 6
INSERT INTO caisse (id_entite, id_facture, montant, date_encaissement, mode_encaissement, `reference`, cree_par) VALUES
(86, 1, 300.00, '2025-12-20 14:35:00', 'ESPECES', 'CAISSE-001', 'salma.sec'),
(87, 2, 2000.00, '2025-12-22 10:20:00', 'CARTE_BANCAIRE', 'CAISSE-002', 'salma.sec'),
(88, 3, 200.00, '2025-12-22 16:50:00', 'ESPECES', 'CAISSE-003', 'salma.sec'),
(89, 5, 400.00, '2025-12-18 11:25:00', 'VIREMENT', 'CAISSE-004', 'salma.sec'),
(90, 6, 500.00, '2025-12-19 15:35:00', 'CARTE_BANCAIRE', 'CAISSE-005', 'salma.sec');

-- 25. FileAttente (dépend de BaseEntity)
INSERT INTO file_attente (id_entite, date_file, capacite, est_ouverte, cree_par) VALUES
(91, '2025-12-24', 10, TRUE, 'salma.sec'),
(92, '2025-12-25', 8, TRUE, 'salma.sec'),
(93, '2025-12-26', 12, FALSE, 'salma.sec');

-- 26. Certificat (dépend de DossierMedicale et Consultation)
-- id_certificat AUTO_INCREMENT: 1-2
-- id_dossier_medical: 1, 6
-- id_consultation: 1, 6 (UNIQUE constraint)
INSERT INTO certificat (id_entite, id_dossier_medical, id_consultation, dateDebut, dateFin, duree, noteMedecin, cree_par) VALUES
(94, 6, 6, '2025-12-19', '2025-12-22', 3, 'Repos strict requis suite à l''extraction dentaire. Éviter les efforts physiques.', 'dr.benani'),
(95, 1, 1, '2025-12-20', '2025-12-23', 3, 'Repos recommandé après traitement de carie.', 'dr.alami');

-- 27. Notification (dépend de BaseEntity)
-- id_notif AUTO_INCREMENT: 1-4
INSERT INTO notification (id_entite, titre, message, date_notif, heure, type_notif, priorite, cree_par) VALUES
(96, 'Rappel rendez-vous', 'Vous avez un rendez-vous prévu le 24/12/2025 à 09:00', '2025-12-23', '08:00:00', 'RAPPEL', 'MOYENNE', 'system'),
(97, 'Alerte facture', 'Facture impayée détectée pour le patient Youssef Dari', '2025-12-23', '10:00:00', 'ALERTE', 'HAUTE', 'system'),
(98, 'Nouveau patient', 'Nouveau patient enregistré: Hiba Zerouali', '2025-10-26', '10:00:00', 'INFO', 'BASSE', 'system'),
(99, 'Rappel consultation', 'Rappel: Consultation prévue demain à 10:30', '2025-12-23', '18:00:00', 'RAPPEL', 'MOYENNE', 'system');

-- 28. Utilisateur_Notification
-- id_user: 1=Marouane, 2=Dr.Alami, 3=Dr.Benani, 4=Salma
-- id_notif: 1-4
INSERT INTO utilisateur_notification (id_user, id_notif, lu, date_lu) VALUES
(1, 1, FALSE, NULL), -- Marouane -> Notification 1
(2, 1, FALSE, NULL), -- Dr. Alami -> Notification 1
(4, 1, TRUE, '2025-12-23 08:05:00'), -- Salma -> Notification 1 (lue)
(1, 2, FALSE, NULL), -- Marouane -> Notification 2
(4, 2, FALSE, NULL), -- Salma -> Notification 2
(1, 3, TRUE, '2025-10-26 10:05:00'), -- Marouane -> Notification 3 (lue)
(2, 4, FALSE, NULL), -- Dr. Alami -> Notification 4
(4, 4, FALSE, NULL); -- Salma -> Notification 4

-- 29. Log (dépend de Utilisateur)
-- id_log AUTO_INCREMENT: 1-5
-- id_utilisateur: 1=Marouane, 2=Dr.Alami, 4=Salma (ou NULL)
INSERT INTO `log` (id_entite, `action`, date_heure, ip_adresse, statut, id_utilisateur, cree_par) VALUES
(100, 'Connexion utilisateur', '2025-12-23 08:30:00', '192.168.1.100', 'SUCCESS', 1, 'system'),
(101, 'Création facture', '2025-12-20 14:30:00', '192.168.1.101', 'SUCCESS', 4, 'system'),
(102, 'Consultation créée', '2025-12-20 09:15:00', '192.168.1.102', 'SUCCESS', 2, 'system'),
(103, 'Modification dossier patient', '2025-12-22 11:20:00', '192.168.1.102', 'SUCCESS', 2, 'system'),
(104, 'Tentative connexion échouée', '2025-12-23 10:15:00', '192.168.1.105', 'FAILED', NULL, 'system');

-- 30. Statistique (dépend de CabinetMedicale)
-- id_statistique AUTO_INCREMENT: 1-4
-- id_cabinet: 1
INSERT INTO statistique (id_entite, nom, categorie, chiffre, date_calcul, id_cabinet, cree_par) VALUES
(105, 'Total patients', 'PATIENT', 6.0, '2025-12-23', 1, 'system'),
(106, 'Total consultations', 'CONSULTATION', 6.0, '2025-12-23', 1, 'system'),
(107, 'Revenus mensuels', 'FINANCIERE', 4200.00, '2025-12-23', 1, 'system'),
(108, 'Performance cabinet', 'PERFORMANCE', 85.5, '2025-12-23', 1, 'system');

-- 31. Charges (dépend de CabinetMedicale)
-- id_charge AUTO_INCREMENT: 1-4
-- id_cabinet: 1
INSERT INTO charges (id_entite, titre, `description`, montant, date_charge, id_cabinet, cree_par) VALUES
(109, 'Achat matériel dentaire', 'Commande de matériel pour le mois de décembre', 5000.00, '2025-12-01 10:00:00', 1, 'admin'),
(110, 'Loyer cabinet', 'Loyer mensuel du cabinet', 8000.00, '2025-12-05 09:00:00', 1, 'admin'),
(111, 'Électricité et eau', 'Factures utilities', 1200.00, '2025-12-10 14:00:00', 1, 'admin'),
(112, 'Formation continue', 'Formation en orthodontie', 3000.00, '2025-12-15 08:00:00', 1, 'admin');

-- 32. Revenues (dépend de CabinetMedicale)
-- id_revenue AUTO_INCREMENT: 1-3
-- id_cabinet: 1
INSERT INTO revenues (id_entite, titre, `description`, montant, date_revenue, id_cabinet, cree_par) VALUES
(113, 'Consultations décembre', 'Revenus des consultations du mois', 4200.00, '2025-12-20 16:00:00', 1, 'admin'),
(114, 'Traitements effectués', 'Revenus des traitements dentaires', 3500.00, '2025-12-22 17:00:00', 1, 'admin'),
(115, 'Prestations esthétiques', 'Blanchiment et autres prestations', 1500.00, '2025-12-18 15:00:00', 1, 'admin');
