package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

public class CompanyDao implements IDataAccessObject<Company> {
    private final EntityManager em;

    /**
     * Créée une instance de Catalogue qui utilisera l'EntityManager passé
     * en argument pour gérer la persistence des objets.
     *
     * @param em l'entity manager en charge de la gestion des objets
     */
    public CompanyDao(EntityManager em) { this.em = em; }

    /**
     * Renvoie la collection de toutes compagnies
     *
     * @return toutes les compagnies
     */
    public Collection<Company> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var q = cb.createQuery(Company.class);
        var r = q.from(Company.class);
        return em.createQuery(q.select(r)).getResultList();
    }

    /**
     * Renvoie une compagnie en fonction de son id.
     *
     * @param id l'id de la compagnie cherchée
     * @return la compagnie trouvée ou null si aucune compagnie n'a été trouvée
     */
    public Company getOneById(String id) {
        return em.find(Company.class, id);
    }

    /**
     * Persiste une compagnie
     *
     * @param c la compagnie à persister
     * @return L'objet compagnie connu par le support de persistence. Doit
     * être equals() à l'objet c.
     */
    public Company save(Company c) {
        if (em.contains(c)) {
            return c;
        } else {
            if (em.find(Company.class, c.getId()) == null) {
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
    public boolean deleteOneById(String id) {
        Company c = em.find(Company.class, id);
        if (c == null) {
            return false;
        } else {
            deleteVolsByCompanyId(id);
            em.remove(c);
            return true;
        }
    }

    /* ON CASCADE */
    /**
     * Supprime tous les vols rattachés à une compagnie
     *
     * @param companyId l'id de la compagnie ciblée
     */
    private void deleteVolsByCompanyId(String companyId) {
        deleteBagagesByCompanyId(companyId);
        var dq = em.createQuery("SELECT v FROM Vol v WHERE v.company.id = :cId", Vol.class);
        dq.setParameter("cId", companyId);
        for(Vol v : dq.getResultList()) {
            em.remove(v);
        }
    }

    /**
     * Supprime tous les bagages rattachés à une compagnie.
     *
     * @param companyId l'id de la compagnie ciblée
     */
    private void deleteBagagesByCompanyId(String companyId) {
        var dq = em.createQuery("SELECT b FROM Bagage b WHERE b.vol.company.id = :cId", Bagage.class);
        dq.setParameter("cId", companyId);
        for(Bagage b : dq.getResultList()) {
            em.remove(b);
        }
    }
}
