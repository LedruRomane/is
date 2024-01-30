package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Compagnie;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

public class CatalogueCompanie {

    private final EntityManager em;

    /**
     * Créée une instance de Catalogue qui utilisera l'EntityManager passé
     * en argument pour gérer la persistence des objets.
     *
     * @param em l'entity manager en charge de la gestion des objets
     */
    public CatalogueCompanie(EntityManager em) {
        this.em = em;
    }

    /**
     * Renvoie la collection de toutes compagnies
     *
     * @return toutes les compagnies
     */
    public Collection<Compagnie> getCompagnies() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var q = cb.createQuery(Compagnie.class);
        var r = q.from(Compagnie.class);
        return em.createQuery(q.select(r)).getResultList();
    }

    /**
     * Renvoie une compagnie en fonction de son id.
     *
     * @param id l'id de la compagnie cherchée
     * @return la compagnie trouvée ou null si aucune compagnie n'a été trouvée
     */
    public Compagnie getCompagnie(String id) {
        return em.find(Compagnie.class, id);
    }

    /**
     * Persiste une compagnie
     *
     * @param c la compagnie à persister
     * @return L'objet compagnie connu par le support de persistence. Doit
     * être equals() à l'objet c.
     */
    public Compagnie saveCompagnie(Compagnie c) {
        if (em.contains(c)) {
            return c;
        } else {
            if (em.find(Compagnie.class, c.getId()) == null) {
                em.persist(c);
                return c;
            } else {
                return em.merge(c);
            }
        }
    }

    /**
     * Supprime une compagnie.
     *
     * @param id l'identifiant de la compagnie à supprimer.
     * @return true si la compagnie à été supprimée.
     */
    public boolean deleteCompagnieById(String id) {
        Compagnie c = em.find(Compagnie.class, id);
        if (c == null) {
            return false;
        } else {
            deleteVolsByCompagnieId(id);
            em.remove(c);
            return true;
        }
    }


    /**
     * Supprime tous les vols rattachés à une compagnie
     *
     * @param compagnieId l'id de la compagnie ciblée
     */
    private void deleteVolsByCompagnieId(String compagnieId) {
        deleteBagagesByCompagnieId(compagnieId);
        var dq = em.createQuery("SELECT v FROM Vol v WHERE v.compagnie.id = :cId", Vol.class);
        dq.setParameter("cId", compagnieId);
        for(Vol v : dq.getResultList()) {
            em.remove(v);
        }
    }

    /**
     * Supprime tous les bagages rattachés à une compagnie.
     *
     * @param compagnieId l'id de la compagnie ciblée
     */
    private void deleteBagagesByCompagnieId(String compagnieId) {
        var dq = em.createQuery("SELECT b FROM Bagage b WHERE b.vol.compagnie.id = :cId", Bagage.class);
        dq.setParameter("cId", compagnieId);
        for(Bagage b : dq.getResultList()) {
            em.remove(b);
        }
    }

}
