package ma.dentalTech.repository.common;

import ma.dentalTech.entities.Acte.Acte;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll() throws SQLException;
    Optional<T> findById(ID id) throws SQLException;
    void create(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(T entity) throws SQLException;
    void deleteById(ID id) throws SQLException;
}

