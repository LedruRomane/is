package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import tiw.is.vols.livraison.db.BaggageKey;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

public class CatalogueBagage {

    private final EntityManager em;
    private final CatalogueVol catalogueVol;

    public CatalogueBagage(EntityManager em, CatalogueVol catalogueVol) {
        this.em = em;
        this.catalogueVol = catalogueVol;
    }

    /**
     * Renvoie l'ensemble des bagages
     *
     * @return une collection de Bagages
     */
    public Collection<Baggage> getBagages() {
        return em.createQuery("SELECT b FROM Baggage b", Baggage.class)
                .getResultList();
    }

    /**
     * Créée un bagage pour le vol indiqué
     *
     * @param vol      le vol concerné par le bagage
     * @param poids    le poids du bagage
     * @param passager la référence du passager ayant déposé le bagage
     * @return le bagage créé et persisté
     */
    public Baggage createBagage(Vol vol, float poids, String passager) {
        Baggage b = vol.createBagage(poids, passager);
        em.persist(b);
        return b;
    }

    /**
     * Renvoie un bagage cherché par identifiant de vol et numéro
     *
     * @param volId  l'identifiant du vol auquel le bagage est rattaché
     * @param numero le numero du bagage
     * @return le bagage cherché ou null si aucun bagage n'a été trouvé
     */
    public Baggage getBagageById(String volId, int numero) {
        Vol vol = catalogueVol.getVol(volId);
        if (vol == null) {
            return null;
        }
        return em.find(Baggage.class, new BaggageKey(vol, numero));
    }

    /**
     * Met à jour un bagage existant (à utiliser à partir d'un BaggageDTO)
     *
     * @param volId    l'ID du vol concerné par le bagage
     * @param poids    le poids du bagage
     * @param passager la référence du passager ayant déposé le bagage
     * @return le bagage mis à jour et persisté ou null si le bagage n'existe
     * pas
     */
    public Baggage updateBagage(String volId, int numero, float poids,
                                String passager, boolean delivre,
                                boolean recupere) {
        Baggage b = getBagageById(volId, numero);
        if (b != null) {
            b.setPoids(poids);
            b.setPassager(passager);
            if (delivre)
                b.delivrer();
            if (recupere)
                b.recuperer();
            em.persist(b);
        }
        return b;
    }

    /**
     * Met à jour un baggage existant (à utiliser à partir d'un Baggage)
     *
     * @param baggage Le baggage à mettre à jour
     * @return le baggage mis à jour et persisté ou null si le baggage n'existe
     * pas
     */
    public Baggage updateBagage(Baggage baggage) {
        return updateBagage(baggage.getVol().getId(), baggage.getNumero(),
                baggage.getPoids(), baggage.getPassager(), baggage.isDelivre(),
                baggage.isRecupere());
    }

    /**
     * Renvoie l'ensemble des bagages non délivrés pour un vol
     *
     * @param volId l'ID du vol concerné
     * @return une collection de Baggage sur ce vol dont l'attribut delivre
     * est false
     */
    public Collection<Baggage> getBagagesPerdusByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b" +
                        " WHERE b.vol.id = :vId" +
                        " AND b.delivre is false", Baggage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

    /**
     * Renvoie l'ensemble des bagages non récupérés pour un vol
     *
     * @param volId l'ID du vol concerné
     * @return une collection de Baggage sur ce vol dont l'attribut recupere
     * est false
     */
    public Collection<Baggage> getBagagesNonRecuperesByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b" +
                        " WHERE b.vol.id = :vId" +
                        " AND b.recupere is false", Baggage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

    /**
     * Supprime un bagage
     *
     * @param volId  l'identifiant du vol auquel ce bagage est rattaché
     * @param numero le numéro du bagage
     * @return true si le bagage a été supprimé, false s'il n'a pas été trouvé
     */
    public boolean deleteBagageById(String volId, int numero) {
        Baggage b = getBagageById(volId, numero);
        if (b == null) {
            return false;
        } else {
            em.remove(b);
            return true;
        }
    }

}
