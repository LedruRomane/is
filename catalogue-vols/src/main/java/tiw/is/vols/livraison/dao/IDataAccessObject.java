package tiw.is.vols.livraison.dao;

import java.util.Collection;
import java.util.Map;

public interface IDataAccessObject<O> {

    public Collection<O> getAll();

    default O getOneById(String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // For composed identifiers.
    default O getOneById(String id, int num) {
        throw new UnsupportedOperationException("Not implemented");
    }

    O save(O object);

    default boolean deleteOneById(String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // For composed identifiers.
    default boolean deleteOneById(String id, int num) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
