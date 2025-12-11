package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.enums.MoisEnum;
import ma.dentalTech.entities.enums.JoursEnum;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.conf.SessionFactory;
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
                list.add(mapToAgenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll Agenda", e);
        }
        return list;
    }

    @Override
    public AgendaMensuel save(AgendaMensuel entity) {
        if (entity.getIdEntite() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete Agenda", e);
        }
    }

    @Override
    public Optional<AgendaMensuel> findByMedecinId(Long medecinId) {
        String sql = "SELECT * FROM AgendaMensuel WHERE medecin_id = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, medecinId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToAgenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByMedecinId Agenda", e);
        }
        return Optional.empty();
    }

    @Override
    public List<AgendaMensuel> findByMois(MoisEnum mois) {
        String sql = "SELECT * FROM AgendaMensuel WHERE mois = ?";
        List<AgendaMensuel> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mois.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToAgenda(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByMois Agenda", e);
        }
        return list;
    }

    private AgendaMensuel mapToAgenda(ResultSet rs) throws SQLException {
        AgendaMensuel agenda = AgendaMensuel.builder()
                .mois(MoisEnum.valueOf(rs.getString("mois")))
                .joursNonDisponible(deserializeJours(rs.getString("joursNonDisponible")))
                .build();
        agenda.setIdEntite(rs.getLong("idEntite"));
        
        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) agenda.setDateCreation(LocalDate.from(dc.toLocalDateTime()));
        
        var ddm = rs.getTimestamp("dateDerniereModification");
        if (ddm != null) agenda.setDateDerniereModification(ddm.toLocalDateTime());
        
        agenda.setModifiePar(rs.getString("modifiePar"));
        agenda.setCreePar(rs.getString("creePar"));
        
        // Set medecin if medecin_id exists
        long medecinId = rs.getLong("medecin_id");
        if (!rs.wasNull()) {
            Medecin medecin = new Medecin();
            medecin.setIdEntite(medecinId);
            agenda.setMedecin(medecin);
        }
        
        return agenda;
    }

    private String serializeJours(List<JoursEnum> jours) {
        if (jours == null || jours.isEmpty()) {
            return "";
        }
        return jours.stream()
                .map(JoursEnum::name)
                .collect(Collectors.joining(","));
    }

    private List<JoursEnum> deserializeJours(String str) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        List<JoursEnum> jours = new ArrayList<>();
        String[] parts = str.split(",");
        for (String part : parts) {
            try {
                jours.add(JoursEnum.valueOf(part));
            } catch (IllegalArgumentException _) {}
        }
        return jours;
    }
}