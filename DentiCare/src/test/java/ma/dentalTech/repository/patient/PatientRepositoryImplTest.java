package ma.dentalTech.repository.patient;
/*
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.enums.AssuranceEnum;
import ma.dentalTech.entities.enums.SexeEnum;

import ma.dentalTech.repository.modules.patient.api.patientRepository;
import ma.dentalTech.repository.modules.patient.impl.patientRepositoryImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class PatientRepositoryImplTest {

    private patientRepository repo;

    @BeforeEach
    void setup() {
        // Réinitialise la BD et insère le dataset (6 patients, 12 antécédents + liaisons)
        DbTestUtils.cleanAll();
        DbTestUtils.seedFullDataset();

        repo = new patientRepositoryImpl();
    }

    @Test
    @DisplayName("1) findAll : retourne les 6 patients seedés")
    void testFindAll() throws SQLException {
        List<Patient> list = repo.findAll();
        assertThat(list).hasSize(6);
        assertThat(list).extracting(Patient::getEmail)
                .contains("amal@example.com", "omar@example.com", "nour@example.com");
    }

    @Test
    @DisplayName("2) findById : Omar id=2")
    void testFindById() throws SQLException {
        Patient p = repo.findById(2L);
        assertThat(p).isNotNull();
        assertThat(p.getNom()).isEqualTo("Omar");
        assertThat(p.getEmail()).isEqualTo("omar@example.com");
    }

    @Test
    @DisplayName("3) create + findByEmail")
    void testCreateAndFindByEmail() throws SQLException {
        Patient p = Patient.builder()
                .nom("Dina")
                .adresse("Rabat-Agdal")
                .telephone("0707070707")
                .email("dina@example.com")
                .dateNaissance(LocalDate.of(2002, 1, 15))
                .dateCreation(LocalDate.from(LocalDateTime.now()))
                .sexe(SexeEnum.FEMININ)
                .assurance(AssuranceEnum.AUCUNE)
                .build();

        repo.create(p);
        assertThat(p.getIdPatient()).isNotNull();

        var found = repo.findByEmail("dina@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Dina");

    }

    @Test
    @DisplayName("4) update : modifie adresse & téléphone")
    void testUpdate() {
        Patient p = repo.findById(1L); // Amal
        p.setAdresse("Kénitra");
        p.setTelephone("0700000000");
        repo.update(p);

        Patient updated = repo.findById(1L);
        assertThat(updated.getAdresse()).isEqualTo("Kénitra");
        assertThat(updated.getTelephone()).isEqualTo("0700000000");
    }

    @Test
    @DisplayName("5) deleteById : supprime un patient")
    void testDeleteById() {
        long before = repo.count();
        repo.deleteById(6L); // Mahdi
        assertThat(repo.findById(6L)).isNull();
        assertThat(repo.count()).isEqualTo(before - 1);
    }

    @Test
    @DisplayName("6) searchByNomPrenom : filtre sur 'Am'")
    void testSearchByNomPrenom() {
        var list = repo.searchByNomPrenom("Am");
        assertThat(list).extracting(Patient::getNom).contains("Amal");
    }

    @Test
    @DisplayName("7) existsById / count / findPage")
    void testExistsCountPaging() {
        assertThat(repo.existsById(1L)).isTrue();
        assertThat(repo.existsById(999L)).isFalse();
        assertThat(repo.count()).isEqualTo(6);

        var page = repo.findPage(2, 0);
        assertThat(page).hasSize(2);
    }

    @Test
    @DisplayName("8) Many-to-Many : getAntecedentsOfPatient(1) puis modifications")
    void testManyToManyReadWrite() {
        // Dataset : Patient 1 (Amal) → (2: allergie latex), (4: diabète T2)
        var a1 = repo.getAntecedentsOfPatient(1L);
        assertThat(a1).extracting("id").containsExactlyInAnyOrder(2L, 4L);

        // Ajoute allergie pénicilline (id=1)
        repo.addAntecedentToPatient(1L, 1L);
        var a2 = repo.getAntecedentsOfPatient(1L);
        assertThat(a2).extracting("id").containsExactlyInAnyOrder(1L, 2L, 4L);

        // Retire le diabète (id=4)
        repo.removeAntecedentFromPatient(1L, 4L);
        var a3 = repo.getAntecedentsOfPatient(1L);
        assertThat(a3).extracting("id").containsExactlyInAnyOrder(1L, 2L);

        // Supprime tout
        repo.removeAllAntecedentsFromPatient(1L);
        var a4 = repo.getAntecedentsOfPatient(1L);
        assertThat(a4).isEmpty();
    }

    @Test
    @DisplayName("9) Navigation inverse : getPatientsByAntecedent(2: allergie latex)")
    void testGetPatientsByAntecedent() {
        var patients = repo.getPatientsByAntecedent(2L); // allergie latex
        // Dataset initial : patient 1 (Amal) et 2 (Omar) ont l'id=2
        assertThat(patients).extracting(Patient::getId)
                .contains(1L, 2L);
    }
}
*/