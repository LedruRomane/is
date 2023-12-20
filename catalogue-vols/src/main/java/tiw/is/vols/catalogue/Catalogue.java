package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import tiw.is.vols.catalogue.modeles.Vol;

import java.util.Collection;

public class Catalogue {

    private EntityManagerFactory emf;

    public Catalogue(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Collection<Vol> getVols() {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(Vol.class);
            var root = cq.from(Vol.class);
            var cq2 = cq.select(root);
            var tq = em.createQuery(cq2);
            return tq.getResultList();
        }
    }
}
