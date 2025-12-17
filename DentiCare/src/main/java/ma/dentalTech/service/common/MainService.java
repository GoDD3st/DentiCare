package ma.dentalTech.service.common;

import java.util.List;
import java.util.Optional;


public interface MainService<T, ID> {
    List<T> findAll() throws Exception;

    Optional<T> findByID(ID id) throws Exception;

    T create(T item);

    T update(ID id,T item);

    T delete(T item);

    void deleteByID(ID id);
}
