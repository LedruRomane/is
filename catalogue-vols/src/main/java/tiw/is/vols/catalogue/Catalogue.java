package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import tiw.is.vols.catalogue.modeles.Vol;

import java.util.Collection;

public class Catalogue {

    private EntityManager em;

    public Catalogue(EntityManager em) {
        this.em = em;
    }

    public Collection<Vol> getVols() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Vol.class);
        var root = cq.from(Vol.class);
        var cq2 = cq.select(root);
        var tq = em.createQuery(cq2);
        return tq.getResultList();
    }

    public Vol getVol(String id) {
        return em.find(Vol.class, id);
    }

    /**
     * Sauvegarde au besoin un vol.
     * @param v le vol à sauvegarder
     * @return l'objet représentant v géré par l'entitymanager, qui peut être v lui-même
     */
    public Vol saveVol(Vol v) {
        if (em.contains(v)) {
            return em.find(Vol.class, v.getId());
        } else {
            em.persist(v);
            return v;
        }
    }
}
