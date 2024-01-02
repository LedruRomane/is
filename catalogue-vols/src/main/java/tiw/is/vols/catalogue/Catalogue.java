package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import tiw.is.vols.catalogue.modeles.Bagage;
import tiw.is.vols.catalogue.modeles.BagageKey;
import tiw.is.vols.catalogue.modeles.Companie;
import tiw.is.vols.catalogue.modeles.Vol;

import java.util.Collection;

/**
 * Classe permettant d'accéder aux données du catalogue.
 */
public class Catalogue {

    private final EntityManager em;

    /**
     * Constructeur pour créer une instance de Catalogue avec un EntityManager spécifié.
     *
     * @param em l'EntityManager à utiliser pour les opérations de base de données.
     */
    public Catalogue(EntityManager em) {
        this.em = em;
    }

    /**
     * Récupère tous les vols disponibles.
     *
     * @return une collection de tous les vols.
     */
    public Collection<Vol> getVols() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Vol.class);
        var root = cq.from(Vol.class);
        var cq2 = cq.select(root);
        var tq = em.createQuery(cq2);
        return tq.getResultList();
    }

    /**
     * Récupère un vol spécifique par son identifiant.
     *
     * @param id l'identifiant du vol à récupérer.
     * @return l'objet Vol correspondant à l'identifiant spécifié, ou null si aucun vol n'est trouvé.
     */
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

    /**
     * Récupère toutes les compagnies disponibles.
     *
     * @return une collection de toutes les compagnies.
     */
    public Collection<Companie> getCompanies() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Companie.class);
        var root = cq.from(Companie.class);
        var cq2 = cq.select(root);
        var tq = em.createQuery(cq2);
        return tq.getResultList();
    }

    /**
     * Récupère une compagnie spécifique par son identifiant.
     * @param id l'identifiant de la compagnie à récupérer.
     * @return l'objet Companie correspondant à l'identifiant spécifié, ou null si aucune compagnie n'est trouvée.
     */
    public Companie getCompany(String id) {
        return em.find(Companie.class, id);
    }

    /**
     * Sauvegarde au besoin une compagnie.
     * @param c la compagnie à sauvegarder.
     * @return l'objet représentant c géré par l'entitymanager, qui peut être c lui-même.
     */
    public Companie saveCompany(Companie c) {
        if (em.contains(c)) {
            return em.find(Companie.class, c.getId());
        } else {
            em.persist(c);
            return c;
        }
    }

    /**
     * Supprime un vol par son identifiant.
     * @param id l'identifiant du vol à supprimer.
     * @return true si le vol a été supprimé, false sinon.
     */
    public boolean deleteVolById(String id) {
        Vol v = em.find(Vol.class, id);
        if (v != null) {
            em.remove(v);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Supprime une compagnie par son identifiant.
     * @param id l'identifiant de la compagnie à supprimer.
     * @return true si la compagnie a été supprimée, false sinon.
     */
    public boolean deleteCompanyById(String id) {
        Companie c = em.find(Companie.class, id);
        if (c != null) {
            em.remove(c);
            return true;
        } else {
            return false;
        }
    }

    Bagage createBagage(BagageKey bagageKey, double poids, String passagerRef) {
        Bagage b = new Bagage(bagageKey, poids, passagerRef);
        em.persist(b);
        return b;
    }

    Bagage getBagageById(String volId, int number) {
        return em.find(Bagage.class, new BagageKey(getVol(volId), number));
    }

    boolean deleteBagageById(String volId, int number) {
        Bagage b = getBagageById(volId, number);
        if (b != null) {
            em.remove(b);
            return true;
        } else {
            return false;
        }
    }
}
