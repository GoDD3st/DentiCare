package ma.dentalTech.repository.modules.certificat.impl.mySQL;

import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.repository.modules.certificat.api.CertificatRepository;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CertificatRepositoryImpl implements CertificatRepository {
    
    @Override
    public Optional<Certificat> findById(Long id) {
        String sql = "SELECT * FROM Certificat WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToCertificat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du certificat", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Certificat> findAll() {
        String sql = "SELECT * FROM Certificat";
        List<Certificat> certificats = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                certificats.add(mapToCertificat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des certificats", e);
        }
        return certificats;
    }
    
    @Override
    public Certificat save(Certificat certificat) {
        if (certificat.getIdEntite() == null) {
            return insert(certificat);
        } else {
            return update(certificat);
        }
    }
    
    private Certificat insert(Certificat certificat) {
        String sql = "INSERT INTO Certificat (dateDebut, dateFin, duree, noteMedecin, patient_id, medecin_id, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(certificat.getDateDebut()));
            stmt.setDate(2, Date.valueOf(certificat.getDateFin()));
            stmt.setInt(3, certificat.getDuree());
            stmt.setString(4, certificat.getNoteMedecin());
            stmt.setLong(5, certificat.getPatient().getIdEntite());
            stmt.setLong(6, certificat.getMedecin().getIdEntite());
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                certificat.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du certificat", e);
        }
        return certificat;
    }
    
    private Certificat update(Certificat certificat) {
        String sql = "UPDATE Certificat SET dateDebut = ?, dateFin = ?, duree = ?, noteMedecin = ?, patient_id = ?, medecin_id = ?, dateDerniereModification = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(certificat.getDateDebut()));
            stmt.setDate(2, Date.valueOf(certificat.getDateFin()));
            stmt.setInt(3, certificat.getDuree());
            stmt.setString(4, certificat.getNoteMedecin());
            stmt.setLong(5, certificat.getPatient().getIdEntite());
            stmt.setLong(6, certificat.getMedecin().getIdEntite());
            stmt.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(8, certificat.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du certificat", e);
        }
        return certificat;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Certificat WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du certificat", e);
        }
    }
    
    @Override
    public List<Certificat> findByPatientId(Long patientId) {
        String sql = "SELECT * FROM Certificat WHERE patient_id = ?";
        List<Certificat> certificats = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapToCertificat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des certificats", e);
        }
        return certificats;
    }
    
    @Override
    public List<Certificat> findByMedecinId(Long medecinId) {
        String sql = "SELECT * FROM Certificat WHERE medecin_id = ?";
        List<Certificat> certificats = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, medecinId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapToCertificat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des certificats", e);
        }
        return certificats;
    }
    
    private Certificat mapToCertificat(ResultSet rs) throws SQLException {
        return Certificat.builder()
                .idEntite(rs.getLong("idEntite"))
                .dateDebut(rs.getDate("dateDebut").toLocalDate())
                .dateFin(rs.getDate("dateFin").toLocalDate())
                .duree(rs.getInt("duree"))
                .noteMedecin(rs.getString("noteMedecin"))
                .build();
    }
}

