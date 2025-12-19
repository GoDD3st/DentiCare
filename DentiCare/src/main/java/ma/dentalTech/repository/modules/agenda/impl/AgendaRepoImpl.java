package ma.dentalTech.repository.modules.personnel.impl;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.repository.modules.agenda.api.AgendaRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AgendaRepoImpl implements AgendaRepository {

    @Override
    public List<AgendaMensuel> findAll() throws SQLException {
        String sql = "SELECT * FROM agenda_mensuel ORDER BY id_agenda";
        List<AgendaMensuel> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapAgendaMensuel(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<AgendaMensuel> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM agenda_mensuel WHERE id_agenda = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapAgendaMensuel(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(AgendaMensuel am) throws SQLException {
        String sql = "INSERT INTO agenda_mensuel (mois, jours_non_disponible, id_medecin, cree_par) VALUES (?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String joursStr = (am.getJoursNonDisponible() != null) ?
                    am.getJoursNonDisponible().stream().map(Enum::name).collect(Collectors.joining(",")) : null;

            Long idMed = (am.getMedecin() != null) ? am.getMedecin().getIdMedecin() : null;

            ps.setString(1, am.getMois() != null ? am.getMois().name() : null);
            ps.setString(2, joursStr);
            ps.setObject(3, idMed);
            ps.setString(4, am.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    am.setIdAgenda(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(AgendaMensuel am) throws SQLException {
        String sql = "UPDATE agenda_mensuel SET mois = ?, jours_non_disponible = ?, id_medecin = ?, modifie_par = ? WHERE id_agenda = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            String joursStr = (am.getJoursNonDisponible() != null) ?
                    am.getJoursNonDisponible().stream().map(Enum::name).collect(Collectors.joining(",")) : null;

            Long idMed = (am.getMedecin() != null) ? am.getMedecin().getIdMedecin() : null;

            ps.setString(1, am.getMois() != null ? am.getMois().name() : null);
            ps.setString(2, joursStr);
            ps.setObject(3, idMed);
            ps.setString(4, am.getModifiePar());
            ps.setLong(5, am.getIdAgenda());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(AgendaMensuel am) throws SQLException {
        if (am != null && am.getIdAgenda() != null) {
            deleteById(am.getIdAgenda());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM agenda_mensuel WHERE id_agenda = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}