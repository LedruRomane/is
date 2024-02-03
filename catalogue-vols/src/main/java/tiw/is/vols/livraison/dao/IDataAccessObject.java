package tiw.is.vols.livraison.dao;

import java.util.Collection;
import java.util.Map;

public interface IDataAccessObject<O> {

    public Collection<O> getAll();

    default public O getOneById(String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default public O getOneById(String id, int num) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public O save(O object);

    default public boolean deleteOneById(String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default public boolean deleteOneById(String id, int num) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
