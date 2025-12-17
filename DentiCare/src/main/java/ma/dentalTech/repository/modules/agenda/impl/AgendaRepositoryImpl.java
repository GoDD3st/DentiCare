package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;

import ma.dentalTech.repository.modules.agenda.AgendaUtils;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgendaRepositoryImpl implements AgendaRepository {

    @Override
    public Optional<AgendaMensuel> findById(Long id) {
        String sql = "SELECT * FROM AgendaMensuel WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapAgenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById Agenda", e);
        }
        return Optional.empty();
    }


    @Override
    public List<AgendaMensuel> findAll() {
        String sql = "SELECT * FROM AgendaMensuel";
        List<AgendaMensuel> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapAgenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll Agenda", e);
        }
        return list;
    }

    @Override
    public void create(AgendaMensuel agenda) {
        String sql = "INSERT INTO AgendaMensuel (mois, joursNonDisponible, medecin_id, dateCreation) VALUES (?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, agenda.getMois().name());
            stmt.setString(2, AgendaUtils.serializeJours(agenda.getJoursNonDisponible()));

            if (agenda.getMedecin() != null) {
                stmt.setLong(3, agenda.getMedecin().getIdEntite());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }

            stmt.setDate(4, Date.valueOf(LocalDate.now()));

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                agenda.setIdEntite(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insert Agenda", e);
        }
        return agenda;
    }

    private AgendaMensuel update(AgendaMensuel agenda) {
        String sql = "UPDATE AgendaMensuel SET mois=?, joursNonDisponible=?, medecin_id=?, dateDerniereModification=? WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, agenda.getMois().name());
            stmt.setString(2, AgendaUtils.serializeJours(agenda.getJoursNonDisponible()));

            if (agenda.getMedecin() != null) {
                stmt.setLong(3, agenda.getMedecin().getIdEntite());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }

            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(5, agenda.getIdEntite());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update Agenda", e);
        }
        return agenda;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM AgendaMensuel WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete Agenda", e);
        }
    }



}