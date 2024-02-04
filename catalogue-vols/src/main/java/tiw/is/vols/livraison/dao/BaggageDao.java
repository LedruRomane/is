package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import tiw.is.vols.livraison.db.BaggageKey;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;

public class BaggageDao implements IDataAccessObject<Baggage> {
    private final EntityManager em;
    private final FlightDao flightDao;

    public BaggageDao(EntityManager em, FlightDao flightDao) {
        this.em = em;
        this.flightDao = flightDao;
    }

    /**
     * Renvoie l'ensemble des bagages
     *
     * @return une collection de Bagages
     */
    public Collection<Baggage> getAll() {
        return em.createQuery("SELECT b FROM Baggage b", Baggage.class)
                .getResultList();
    }

    /**
     * Créée un bagage pour le vol indiqué
     *
     * @param baggage  Bagage à sauvegarder.
     * @return le bagage créé et persisté
     */
    public Baggage save(Baggage baggage) {
        em.persist(baggage);
        return baggage;
    }

    /**
     * Renvoie un bagage cherché par identifiant de vol et numéro
     *
     * @param flightId  l'identifiant du vol auquel le bagage est rattaché
     * @param numero le numero du bagage
     * @return le bagage cherché ou null si aucun bagage n'a été trouvé
     */
    public Baggage getOneById(String flightId, int numero) {
        Flight flight = flightDao.getOneById(flightId);
        if (flight == null) {
            return null;
        }
        return em.find(Baggage.class, new BaggageKey(flight, numero));
    }

    /**
     * Met à jour un bagage existant (à utiliser à partir d'un BaggageDTO)
     *
     * @param flightId    l'ID du vol concerné par le bagage
     * @param weight    le poids du bagage
     * @param passenger la référence du passager ayant déposé le bagage
     * @return le bagage mis à jour et persisté ou null si le bagage n'existe
     * pas
     */
    public Baggage update(String flightId, int numero, float weight,
                                String passenger, boolean delivre,
                                boolean recupere) {
        Baggage b = getOneById(flightId, numero);
        if (b != null) {
            b.setWeight(weight);
            b.setPassenger(passenger);
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
    public Baggage update(Baggage baggage) {
        return update(baggage.getFlight().getId(), baggage.getNumero(),
                baggage.getWeight(), baggage.getPassenger(), baggage.isDelivre(),
                baggage.isRecupere());
    }

    /**
     * Renvoie l'ensemble des bagages non délivrés pour un vol
     *
     * @param flightId l'ID du vol concerné
     * @return une collection de Baggage sur ce vol dont l'attribut delivre
     * est false
     */
    public Collection<Baggage> getBagagesPerdusByFlightId(String flightId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b" +
                        " WHERE b.flight.id = :vId" +
                        " AND b.delivre is false", Baggage.class);
        dq.setParameter("vId", flightId);
        return dq.getResultList();
    }

    /**
     * Renvoie l'ensemble des bagages non récupérés pour un vol
     *
     * @param flightId l'ID du vol concerné
     * @return une collection de Baggage sur ce vol dont l'attribut recupere
     * est false
     */
    public Collection<Baggage> getBagagesNonRecuperesByFlightId(String flightId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b" +
                        " WHERE b.flight.id = :vId" +
                        " AND b.delivre is true" +
                        " AND b.recupere is false", Baggage.class);
        dq.setParameter("vId", flightId);
        return dq.getResultList();
    }

    /**
     * Supprime un bagage
     *
     * @param flightId  l'identifiant du vol auquel ce bagage est rattaché
     * @param numero le numéro du bagage
     * @return true si le bagage a été supprimé, false s'il n'a pas été trouvé
     */
    public boolean deleteOneById(String flightId, int numero) {
        Baggage b = getOneById(flightId, numero);
        if (b == null) {
            return false;
        } else {
            em.remove(b);
            return true;
        }
    }
}
