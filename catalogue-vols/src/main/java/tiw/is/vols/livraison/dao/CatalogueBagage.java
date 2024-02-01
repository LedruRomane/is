package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import tiw.is.vols.livraison.db.BagageKey;
import tiw.is.vols.livraison.model.Bagage;
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
    public Collection<Bagage> getBagages() {
        return em.createQuery("SELECT b FROM Bagage b", Bagage.class)
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
    public Bagage createBagage(Vol vol, float poids, String passager) {
        Bagage b = vol.createBagage(poids, passager);
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
    public Bagage getBagageById(String volId, int numero) {
        Vol vol = catalogueVol.getVol(volId);
        if (vol == null) {
            return null;
        }
        return em.find(Bagage.class, new BagageKey(vol, numero));
    }

    /**
     * Met à jour un bagage existant (à utiliser à partir d'un BagageDTO)
     *
     * @param volId    l'ID du vol concerné par le bagage
     * @param poids    le poids du bagage
     * @param passager la référence du passager ayant déposé le bagage
     * @return le bagage mis à jour et persisté ou null si le bagage n'existe
     * pas
     */
    public Bagage updateBagage(String volId, int numero, float poids,
                               String passager, boolean delivre,
                               boolean recupere) {
        Bagage b = getBagageById(volId, numero);
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
     * Met à jour un bagage existant (à utiliser à partir d'un Bagage)
     *
     * @param bagage Le bagage à mettre à jour
     * @return le bagage mis à jour et persisté ou null si le bagage n'existe
     * pas
     */
    public Bagage updateBagage(Bagage bagage) {
        return updateBagage(bagage.getVol().getId(), bagage.getNumero(),
                bagage.getPoids(), bagage.getPassager(), bagage.isDelivre(),
                bagage.isRecupere());
    }

    /**
     * Renvoie l'ensemble des bagages non délivrés pour un vol
     *
     * @param volId l'ID du vol concerné
     * @return une collection de Bagage sur ce vol dont l'attribut delivre
     * est false
     */
    public Collection<Bagage> getBagagesPerdusByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Bagage b" +
                        " WHERE b.vol.id = :vId" +
                        " AND b.delivre is false", Bagage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

    /**
     * Renvoie l'ensemble des bagages non récupérés pour un vol
     *
     * @param volId l'ID du vol concerné
     * @return une collection de Bagage sur ce vol dont l'attribut recupere
     * est false
     */
    public Collection<Bagage> getBagagesNonRecuperesByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Bagage b" +
                        " WHERE b.vol.id = :vId" +
                        " AND b.recupere is false", Bagage.class);
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
        Bagage b = getBagageById(volId, numero);
        if (b == null) {
            return false;
        } else {
            em.remove(b);
            return true;
        }
    }

}
