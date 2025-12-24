package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.repository.modules.dossierMedicale.api.CertificatRepo;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.dentalTech.repository.common.RowMappers.mapCertificat;

public class CertificatRepoImpl implements CertificatRepo {
    @Override
    public List<Certificat> findAll() throws SQLException{
        String sql = "SELECT * FROM certificat";
        List<Certificat> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) list.add(mapCertificat(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Certificat> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM certificat WHERE id_certificat = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapCertificat(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Certificat c) {
        String sql = "INSERT INTO certificat (dateDebut, dateFin, duree, noteMedecin) VALUES (?, ?, ?, ?)";
        try (Connection con = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(c.getDateDebut()));
            ps.setDate(2, Date.valueOf(c.getDateFin()));
            ps.setInt(3, c.getDuree());
            ps.setString(4, c.getNoteMedecin());
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setIdCertificat(keys.getLong(1));}
            }

        } catch (SQLException e)
        { throw new RuntimeException(e); }
    }

    @Override
    public void update(Certificat c) {
        String sql = "UPDATE Certificat SET dateDebut = ?, dateFin = ?, duree = ?, noteMedecin = ? WHERE id_ = ?";
        try (Connection con = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(c.getDateDebut()));
            ps.setDate(2, Date.valueOf(c.getDateFin()));
            ps.setInt(3, c.getDuree());
            ps.setString(4, c.getNoteMedecin());
            ps.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setLong(6, c.getIdCertificat());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Certificat c) throws SQLException {
        if (c != null) deleteById(c.getIdCertificat());
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM Certificat WHERE id_certificat = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
