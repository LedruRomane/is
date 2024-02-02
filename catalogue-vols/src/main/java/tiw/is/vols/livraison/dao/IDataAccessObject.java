package tiw.is.vols.livraison.dao;

import java.util.Collection;

public interface IDataAccessObject<O> {

    public Collection<O> getAll();

    public O getOneById(String id);

    public O save(O object);

    public boolean deleteOneById(String id);
}
