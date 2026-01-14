package ma.dentalTech.service.common;

import java.util.List;
import java.util.Optional;


public interface MainService<T, ID> {
    List<T> findAll() throws Exception;

    Optional<T> findByID(ID id) throws Exception;

    T create(T item) throws Exception;

    T update(ID id,T item) throws Exception;

    T delete(T item) throws Exception;

    void deleteByID(ID id) throws Exception;
}
