package ma.dentalTech.repository.modules.cabinetMedicale.impl;

import ma.dentalTech.entities.Statistique.Statistique;
import ma.dentalTech.repository.modules.cabinetMedicale.api.statistiqueRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class statistiqueRepoImpl implements statistiqueRepo {

    @Override
    public List<Statistique> findAll() throws SQLException {
        String sql = "SELECT t.id_statistique, t.nom, t.categorie, t.chiffre, t.date_calcul, t.id_cabinet, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM statistique t " +
                "JOIN base_entity e ON t.id_statistique = e.id_entite " +
                "ORDER BY t.date_calcul DESC";

        List<Statistique> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapStatistique(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Statistique> findById(Long id) throws SQLException {
        String sql = "SELECT t.id_statistique, t.nom, t.categorie, t.chiffre, t.date_calcul, t.id_cabinet, " +
                "e.date_creation, e.cree_par, e.date_derniere_modification, e.modifie_par " +
                "FROM statistique t " +
                "JOIN base_entity e ON t.id_statistique = e.id_entite " +
                "WHERE t.id_statistique = ?";

        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapStatistique(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Statistique s) throws SQLException {
        String sql = "INSERT INTO statistique (id_statistique, nom, categorie, chiffre, date_calcul, id_cabinet) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idCab = (s.getCabinetMedicale() != null) ? s.getCabinetMedicale().getIdCabinet() : null;

            ps.setLong(1, s.getIdStatistique());
            ps.setString(2, s.getNom());
            ps.setString(3, s.getCategorie() != null ? s.getCategorie().name() : null);
            ps.setObject(4, s.getChiffre());
            ps.setDate(5, s.getDateCalcul() != null ? Date.valueOf(s.getDateCalcul()) : null);
            ps.setObject(6, idCab);

            ps.executeUpdate();
        }
    }

    @Override
    public void update(Statistique s) throws SQLException {
        String sql = "UPDATE statistique SET nom = ?, categorie = ?, chiffre = ?, date_calcul = ?, id_cabinet = ? WHERE id_statistique = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idCab = (s.getCabinetMedicale() != null) ? s.getCabinetMedicale().getIdCabinet() : null;

            ps.setString(1, s.getNom());
            ps.setString(2, s.getCategorie() != null ? s.getCategorie().name() : null);
            ps.setObject(3, s.getChiffre());
            ps.setDate(4, s.getDateCalcul() != null ? Date.valueOf(s.getDateCalcul()) : null);
            ps.setObject(5, idCab);
            ps.setLong(6, s.getIdStatistique());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Statistique s) throws SQLException {
        if (s != null && s.getIdStatistique() != null) {
            deleteById(s.getIdStatistique());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM statistique WHERE id_statistique = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}