package ma.dentalTech.repository.modules.agenda.impl.mySQL;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.enums.MoisEnum;
import ma.dentalTech.entities.enums.JoursEnum;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AgendaRepositoryImpl implements AgendaRepository {

    @Override
    public Optional<AgendaMensuel> findById(Long id) {
        String sql = "SELECT * FROM AgendaMensuel WHERE idEntite = ?";
        // CORRECTION 1: Gestion du try-catch multi-exceptions
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAgenda(rs));
            }
        } catch (SQLException | InterruptedException e) {
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
                list.add(mapToAgenda(rs));
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException("Erreur findAll Agenda", e);
        }
        return list;
    }

    @Override
    public AgendaMensuel save(AgendaMensuel entity) {
        // CORRECTION 2: insert et update retournent maintenant un objet, donc ce return est valide
        if (entity.getIdEntite() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    // CORRECTION 2: Changement de 'void' à 'AgendaMensuel'
    private AgendaMensuel insert(AgendaMensuel agenda) {
        String sql = "INSERT INTO AgendaMensuel (mois, joursNonDisponible, medecin_id, dateCreation) VALUES (?, ?, ?, ?)";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, agenda.getMois().name());
            stmt.setString(2, serializeJours(agenda.getJoursNonDisponible()));

            // Gestion safe du Medecin pour éviter NullPointerException
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
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException("Erreur insert Agenda", e);
        }
        // CORRECTION 2: Retourner l'objet
        return agenda;
    }

    // CORRECTION 2: Changement de 'void' à 'AgendaMensuel'
    private AgendaMensuel update(AgendaMensuel agenda) {
        String sql = "UPDATE AgendaMensuel SET mois=?, joursNonDisponible=?, medecin_id=?, dateDerniereModification=? WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, agenda.getMois().name());
            stmt.setString(2, serializeJours(agenda.getJoursNonDisponible()));

            if (agenda.getMedecin() != null) {
                stmt.setLong(3, agenda.getMedecin().getIdEntite());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }

            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(5, agenda.getIdEntite());

            stmt.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException("Erreur update Agenda", e);
        }
        // CORRECTION 2: Retourner l'objet
        return agenda;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM AgendaMensuel WHERE idEntite=?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException("Erreur delete Agenda", e);
        }
    }

    @Override
    public Optional<Agenda