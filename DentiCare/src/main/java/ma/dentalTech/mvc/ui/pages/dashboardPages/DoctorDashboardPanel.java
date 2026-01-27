package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.components.BaseDashboardPanel;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class DoctorDashboardPanel extends BaseDashboardPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    private ma.dentalTech.service.modules.patient.api.PatientService patientService;
    private ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ActeService acteService;
    private ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService ordonnanceService;
    private ma.dentalTech.service.modules.dossierMedicale.api.CertificatService certificatService;
    private ma.dentalTech.service.modules.dossierMedicale.api.InterventionService interventionService;
    private ma.dentalTech.service.modules.facture.api.FactureService factureService;
    private ma.dentalTech.service.modules.finance.api.SituationFinanciereService situationFinanciereService;
    private ma.dentalTech.service.modules.finance.api.CaisseService caisseService;

    // Table references for refreshing (comme admin)
    private JTable patientsTable;
    private DefaultTableModel patientsModel;

    public DoctorDashboardPanel(UserPrincipal principal) {
        super(principal);
        // Initialisation lazy des services
    }

    private ma.dentalTech.service.modules.patient.api.PatientService getPatientService() {
        if (patientService == null) {
            try {
                patientService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.patient.api.PatientService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de PatientService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return patientService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService getDossierService() {
        if (dossierService == null) {
            try {
                dossierService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de DossierMedicaleService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dossierService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService getConsultationService() {
        if (consultationService == null) {
            try {
                consultationService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de ConsultationService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return consultationService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.ActeService getActeService() {
        if (acteService == null) {
            try {
                acteService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.ActeService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de ActeService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return acteService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService getOrdonnanceService() {
        if (ordonnanceService == null) {
            try {
                ordonnanceService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de OrdonnanceService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return ordonnanceService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.CertificatService getCertificatService() {
        if (certificatService == null) {
            try {
                certificatService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.CertificatService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de CertificatService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return certificatService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.InterventionService getInterventionService() {
        if (interventionService == null) {
            try {
                interventionService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.InterventionService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de InterventionService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return interventionService;
    }

    private ma.dentalTech.service.modules.facture.api.FactureService getFactureService() {
        if (factureService == null) {
            try {
                // Utiliser le bean par nom car il y a deux interfaces FactureService dans des packages différents
                Object bean = ma.dentalTech.conf.ApplicationContext.getBean("factureService");
                if (bean instanceof ma.dentalTech.service.modules.facture.api.FactureService) {
                    factureService = (ma.dentalTech.service.modules.facture.api.FactureService) bean;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de FactureService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return factureService;
    }

    private ma.dentalTech.service.modules.finance.api.SituationFinanciereService getSituationFinanciereService() {
        if (situationFinanciereService == null) {
            try {
                situationFinanciereService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.finance.api.SituationFinanciereService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de SituationFinanciereService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return situationFinanciereService;
    }

    private ma.dentalTech.service.modules.finance.api.CaisseService getCaisseService() {
        if (caisseService == null) {
            try {
                caisseService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.finance.api.CaisseService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de CaisseService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return caisseService;
    }

    @Override
    protected String getDashboardTitle() {
        return "Dashboard Médecin";
    }

    @Override
    protected JPanel createSpecificContent() {
        System.out.println("=== DÉBUT createSpecificContent (DOCTOR DASHBOARD) ===");

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Header avec sélecteur de période médical
        JPanel headerPanel = createMedicalStatisticsHeader();
        content.add(headerPanel, BorderLayout.NORTH);
        System.out.println("Header médical ajouté");

        // Contenu principal avec statistiques médicales
        JPanel medicalStatsContent = createMedicalStatisticsContent();
        content.add(medicalStatsContent, BorderLayout.CENTER);
        System.out.println("Contenu statistiques médicales ajouté");

        System.out.println("=== FIN createSpecificContent (DOCTOR DASHBOARD) ===");
        System.out.println("Content final - composants: " + content.getComponentCount());
        return content;
    }

    // ==================== HEADER MÉDICAL ====================
    private JPanel createMedicalStatisticsHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Titre
        JLabel titleLabel = new JLabel("Tableau de Bord Médical");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    // ==================== CONTENU STATISTIQUES MÉDICALES ====================
    private JPanel createMedicalStatisticsContent() {
        System.out.println("=== DÉBUT createMedicalStatisticsContent ===");
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section Statistiques médicales générales
        System.out.println("Ajout de la section statistiques médicales générales...");
        JPanel medicalStats = createMedicalStatsSection();
        content.add(medicalStats);
        content.add(Box.createVerticalStrut(20));

        // Section Patients récents
        content.add(createRecentPatientsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Consultations du jour
        content.add(createTodayConsultationsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Revenus médicaux
        content.add(createMedicalRevenueSection());

        System.out.println("=== FIN createMedicalStatisticsContent ===");
        return content;
    }

    // ==================== STATISTIQUES MÉDICALES GÉNÉRALES ====================
    private JPanel createMedicalStatsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Titre de section
        JLabel sectionTitle = new JLabel("Statistiques Médicales");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(52, 58, 64));
        section.add(sectionTitle, BorderLayout.NORTH);

        // Cards de statistiques
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Charger les statistiques réelles depuis la BD
        loadRealStatistics(statsPanel);

        section.add(statsPanel, BorderLayout.CENTER);

        return section;
    }

    // ==================== PATIENTS RÉCENTS ====================
    private JPanel createRecentPatientsSection() {
        JPanel section = createCard("Patients Récents");

        // Créer un tableau pour les patients récents depuis la BD
        String[] columns = {"Nom", "Téléphone", "Email", "Dernière visite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        try {
            if (getPatientService() != null) {
                java.util.List<ma.dentalTech.entities.Patient.Patient> patients = getPatientService().findAll();
                // Prendre les 5 derniers patients
                patients.stream()
                    .limit(5)
                    .forEach(p -> {
                        String nom = p.getNom() != null ? p.getNom() : "";
                        String tel = p.getTelephone() != null ? p.getTelephone() : "";
                        String email = p.getEmail() != null ? p.getEmail() : "";
                        // Trouver la dernière consultation pour ce patient
                        String derniereVisite = "N/A";
                        try {
                            if (getDossierService() != null) {
                                java.util.Optional<ma.dentalTech.entities.DossierMedicale.DossierMedicale> dossierOpt = 
                                    getDossierService().findAll().stream()
                                        .filter(d -> d.getPatient() != null && d.getPatient().getIdPatient().equals(p.getIdPatient()))
                                        .findFirst();
                                if (dossierOpt.isPresent() && getConsultationService() != null) {
                                    java.util.Optional<ma.dentalTech.entities.Consultation.Consultation> lastConsultation = 
                                        getConsultationService().findAll().stream()
                                            .filter(c -> c.getDossierMedicale() != null && 
                                                c.getDossierMedicale().getIdDossier().equals(dossierOpt.get().getIdDossier()))
                                            .max((c1, c2) -> {
                                                if (c1.getDateConsultation() == null) return -1;
                                                if (c2.getDateConsultation() == null) return 1;
                                                return c1.getDateConsultation().compareTo(c2.getDateConsultation());
                                            });
                                    if (lastConsultation.isPresent() && lastConsultation.get().getDateConsultation() != null) {
                                        derniereVisite = lastConsultation.get().getDateConsultation().toString();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // Ignore
                        }
                        model.addRow(new Object[]{nom, tel, email, derniereVisite});
                    });
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des patients récents: " + e.getMessage());
        }

        JTable patientsTable = new JTable(model);
        patientsTable.setRowHeight(30);
        patientsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(patientsTable);
        scrollPane.setPreferredSize(new Dimension(600, 180));

        section.add(scrollPane, BorderLayout.CENTER);

        return section;
    }

    // ==================== CONSULTATIONS DU JOUR ====================
    private JPanel createTodayConsultationsSection() {
        JPanel section = createCard("Consultations du Jour");

        // Créer un tableau pour les consultations d'aujourd'hui depuis la BD
        String[] columns = {"Heure", "Patient", "Statut", "Observation"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        try {
            if (getConsultationService() != null) {
                java.time.LocalDate today = java.time.LocalDate.now();
                getConsultationService().findAll().stream()
                    .filter(c -> c.getDateConsultation() != null && c.getDateConsultation().equals(today))
                    .sorted((c1, c2) -> {
                        if (c1.getHeureConsultation() == null) return 1;
                        if (c2.getHeureConsultation() == null) return -1;
                        return c1.getHeureConsultation().compareTo(c2.getHeureConsultation());
                    })
                    .forEach(c -> {
                        String heure = c.getHeureConsultation() != null ? c.getHeureConsultation().toString() : "";
                        String patient = c.getDossierMedicale() != null && c.getDossierMedicale().getPatient() != null
                            ? c.getDossierMedicale().getPatient().getNom()
                            : "N/A";
                        String statut = c.getStatut() != null ? c.getStatut().name() : "";
                        String observation = c.getObservationMedecin() != null && c.getObservationMedecin().length() > 30
                            ? c.getObservationMedecin().substring(0, 30) + "..."
                            : (c.getObservationMedecin() != null ? c.getObservationMedecin() : "");
                        model.addRow(new Object[]{heure, patient, statut, observation});
                    });
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des consultations: " + e.getMessage());
        }

        JTable consultationsTable = new JTable(model);
        consultationsTable.setRowHeight(30);
        consultationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        consultationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        consultationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(consultationsTable);
        scrollPane.setPreferredSize(new Dimension(600, 180));

        section.add(scrollPane, BorderLayout.CENTER);

        return section;
    }

    // ==================== REVENUS MÉDICAUX ====================
    private JPanel createMedicalRevenueSection() {
        JPanel section = createCard("Revenus Médicaux");

        JPanel revenuePanel = new JPanel(new GridLayout(1, 3, 20, 0));
        revenuePanel.setOpaque(false);
        revenuePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Calculer les revenus réels
        double revenusMois = 0.0;
        double revenusSemaine = 0.0;
        double moyenneConsultation = 0.0;
        
        try {
            if (getFactureService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                java.time.LocalDate firstDayOfWeek = java.time.LocalDate.now().minusDays(java.time.LocalDate.now().getDayOfWeek().getValue() - 1);
                
                java.util.List<ma.dentalTech.entities.Facture.Facture> factures = getFactureService().findAll();
                
                revenusMois = factures.stream()
                    .filter(f -> f.getDateFacture() != null && !f.getDateFacture().toLocalDate().isBefore(firstDayOfMonth))
                    .filter(f -> f.getTotalePaye() != null)
                    .mapToDouble(f -> f.getTotalePaye())
                    .sum();
                
                revenusSemaine = factures.stream()
                    .filter(f -> f.getDateFacture() != null && !f.getDateFacture().toLocalDate().isBefore(firstDayOfWeek))
                    .filter(f -> f.getTotalePaye() != null)
                    .mapToDouble(f -> f.getTotalePaye())
                    .sum();
                
                // Calculer la moyenne par consultation
                long consultationsCount = 0;
                if (getConsultationService() != null) {
                    consultationsCount = getConsultationService().findAll().size();
                }
                if (consultationsCount > 0) {
                    moyenneConsultation = revenusMois / consultationsCount;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des revenus: " + e.getMessage());
        }

        // Revenus du mois
        JPanel monthRevenue = createRevenueCard("Revenus du mois", String.format("%.0f DH", revenusMois), "", new Color(46, 204, 113));

        // Revenus de la semaine
        JPanel weekRevenue = createRevenueCard("Revenus de la semaine", String.format("%.0f DH", revenusSemaine), "", new Color(52, 152, 219));

        // Moyenne par consultation
        JPanel avgRevenue = createRevenueCard("Moyenne par consultation", String.format("%.0f DH", moyenneConsultation), "", new Color(155, 89, 182));

        revenuePanel.add(monthRevenue);
        revenuePanel.add(weekRevenue);
        revenuePanel.add(avgRevenue);

        section.add(revenuePanel, BorderLayout.CENTER);

        return section;
    }

    // ==================== MÉTHODES UTILITAIRES ====================
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(52, 58, 64));
            card.add(titleLabel, BorderLayout.NORTH);
        }

        return card;
    }

    private void addMedicalStatCard(JPanel panel, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(120, 80));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(Color.WHITE);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        panel.add(card);
    }

    private JPanel createRevenueCard(String title, String amount, String change, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        amountLabel.setForeground(Color.WHITE);

        JLabel changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changeLabel.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(amountLabel, BorderLayout.CENTER);
        centerPanel.add(changeLabel, BorderLayout.SOUTH);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadRealStatistics(JPanel statsPanel) {
        try {
            // Patients actifs
            long patientsCount = getPatientService() != null ? getPatientService().findAll().size() : 0;
            addMedicalStatCard(statsPanel, "Patients actifs", String.valueOf(patientsCount), new Color(52, 152, 219));

            // Consultations aujourd'hui
            long consultationsToday = 0;
            if (getConsultationService() != null) {
                java.time.LocalDate today = java.time.LocalDate.now();
                consultationsToday = getConsultationService().findAll().stream()
                    .filter(c -> c.getDateConsultation() != null && c.getDateConsultation().equals(today))
                    .count();
            }
            addMedicalStatCard(statsPanel, "Consultations aujourd'hui", String.valueOf(consultationsToday), new Color(46, 204, 113));

            // Ordonnances prescrites (ce mois)
            long ordonnancesCount = 0;
            if (getOrdonnanceService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                ordonnancesCount = getOrdonnanceService().findAll().stream()
                    .filter(o -> o.getDate() != null && !o.getDate().isBefore(firstDayOfMonth))
                    .count();
            }
            addMedicalStatCard(statsPanel, "Ordonnances prescrites", String.valueOf(ordonnancesCount), new Color(155, 89, 182));

            // Factures générées (ce mois)
            long facturesCount = 0;
            if (getFactureService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                facturesCount = getFactureService().findAll().stream()
                    .filter(f -> f.getDateFacture() != null && !f.getDateFacture().toLocalDate().isBefore(firstDayOfMonth))
                    .count();
            }
            addMedicalStatCard(statsPanel, "Factures générées", String.valueOf(facturesCount), new Color(230, 126, 34));

            // Interventions réalisées (ce mois)
            long interventionsCount = 0;
            if (getInterventionService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                interventionsCount = getInterventionService().findAll().stream()
                    .filter(i -> i.getDateCreation() != null && !i.getDateCreation().isBefore(firstDayOfMonth))
                    .count();
            }
            addMedicalStatCard(statsPanel, "Interventions réalisées", String.valueOf(interventionsCount), new Color(231, 76, 60));

            // Certificats émis (ce mois)
            long certificatsCount = 0;
            if (getCertificatService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                certificatsCount = getCertificatService().findAll().stream()
                    .filter(c -> c.getDateCreation() != null && !c.getDateCreation().isBefore(firstDayOfMonth))
                    .count();
            }
            addMedicalStatCard(statsPanel, "Certificats émis", String.valueOf(certificatsCount), new Color(149, 165, 166));

            // Revenus du mois
            double revenusMois = 0.0;
            if (getFactureService() != null) {
                java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
                revenusMois = getFactureService().findAll().stream()
                    .filter(f -> f.getDateFacture() != null && !f.getDateFacture().toLocalDate().isBefore(firstDayOfMonth))
                    .filter(f -> f.getTotalePaye() != null)
                    .mapToDouble(f -> f.getTotalePaye())
                    .sum();
            }
            addMedicalStatCard(statsPanel, "Revenus du mois", String.format("%.0f DH", revenusMois), new Color(44, 62, 80));

            // Taux d'occupation (calcul basique)
            double tauxOccupation = 0.0;
            if (getConsultationService() != null && consultationsToday > 0) {
                // Calcul simplifié : consultations aujourd'hui / 20 (capacité estimée)
                tauxOccupation = Math.min(100, (consultationsToday / 20.0) * 100);
            }
            addMedicalStatCard(statsPanel, "Taux d'occupation", String.format("%.0f%%", tauxOccupation), new Color(39, 174, 96));

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
            // Valeurs par défaut en cas d'erreur
            addMedicalStatCard(statsPanel, "Patients actifs", "0", new Color(52, 152, 219));
            addMedicalStatCard(statsPanel, "Consultations aujourd'hui", "0", new Color(46, 204, 113));
            addMedicalStatCard(statsPanel, "Ordonnances prescrites", "0", new Color(155, 89, 182));
            addMedicalStatCard(statsPanel, "Factures générées", "0", new Color(230, 126, 34));
            addMedicalStatCard(statsPanel, "Interventions réalisées", "0", new Color(231, 76, 60));
            addMedicalStatCard(statsPanel, "Certificats émis", "0", new Color(149, 165, 166));
            addMedicalStatCard(statsPanel, "Revenus du mois", "0 DH", new Color(44, 62, 80));
            addMedicalStatCard(statsPanel, "Taux d'occupation", "0%", new Color(39, 174, 96));
        }
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService getOrdonnanceService() {
        try {
            return ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService.class);
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== MÉTHODES UTILITAIRES ====================
    private JButton createIconButton(String iconName, Color bgColor) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(32, 32));
        button.setBackground(bgColor);
        button.setOpaque(true);

        // Charger l'icône
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/icons/" + iconName + ".png");
            Image scaledImage = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            // Fallback vers du texte si l'icône n'existe pas
            button.setText(iconName.substring(0, 1).toUpperCase());
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }

        return button;
    }
}