package ma.dentalTech.repository.modules.medecin.impl;

import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.modules.medecin.api.MedecinRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedecinRepositoryImpl implements MedecinRepository {
    @Override
    public Optional<Medecin> findById(Long id) throws InterruptedException {
        String sql = "SELECT * FROM Medecin WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapMedecin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du medecin", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Medecin> findAll() {
        String sql = "SELECT * FROM Medecin";
        List<Medecin> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapMedecin(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des medecins", e);
        }
        return list;
    }

    @Override
    public void create(Medecin m) {
        if (m.getIdEntite() == null) {
            throw new RuntimeException("idEntite (Staff PK) is required to insert a Medecin");
        }
        String sql = "INSERT INTO Medecin (idEntite, specialite, agendaMensuel_id) VALUES (?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, m.getIdEntite());
            stmt.setString(2, m.getSpecialite());
            if (m.getAgendaMensuel() != null && m.getAgendaMensuel().getIdEntite() != null) {
                stmt.setLong(3, m.getAgendaMensuel().getIdEntite());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du medecin", e);
        }
        return m;
    }

    private Medecin update(Medecin m) {
        if (m.getIdEntite() == null) {
            throw new RuntimeException("idEntite is required to update a Medecin");
        }
        String sql = "UPDATE Medecin SET specialite = ?, agendaMensuel_id = ? WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getSpecialite());
            if (m.getAgendaMensuel() != null && m.getAgendaMensuel().getIdEntite() != null) {
                stmt.setLong(2, m.getAgendaMensuel().getIdEntite());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            stmt.setLong(3, m.getIdEntite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du medecin", e);
        }
        return m;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Medecin WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du medecin", e);
        }
    }

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        String sql = "SELECT * FROM Medecin WHERE specialite = ?";
        List<Medecin> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specialite);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(RowMappers.mapMedecin(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des medecins par specialite", e);
        }
        return list;
    }
}
