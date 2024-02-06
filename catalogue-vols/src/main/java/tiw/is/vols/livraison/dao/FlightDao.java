package tiw.is.vols.livraison.dao;

import jakarta.persistence.EntityManager;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;

/**
 * Data Access Object for Flight Model.
 */
public class FlightDao implements IDataAccessObject<Flight> {

    private final EntityManager em;

    public FlightDao(EntityManager em) {
        this.em = em;
    }

    /**
     * L'ensemble des vols présents en base.
     *
     * @return la liste contenant les vols.
     */
    public Collection<Flight> getAll() {
        return em.createQuery("SELECT v FROM Flight v", Flight.class).getResultList();
    }

    /**
     * Renvoie un vol en fonction de son id.
     *
     * @param id l'id du vol recherché.
     * @return le vol ou null s'il n'a pas été trouvé.
     */
    public Flight getOneById(String id) {
        return em.find(Flight.class, id);
    }

    /**
     * Sauvegarde au besoin un vol.
     *
     * @param v le vol à sauvegarder.
     * @return l'objet représentant v géré par l'entitymanager, qui peut être v lui-même.
     */
    public Flight save(Flight v) {
        if (em.contains(v)) {
            return v;
        } else {
            Flight v2 = em.find(Flight.class, v.getId());
            if (v2 == null) {
                em.persist(v);
                return v;
            } else {
                return em.merge(v);
            }
        }
    }

    /**
     * Supprime un vol spécifié par son id.
     *
     * @param id l'id du vol à supprimer.
     * @return true si le vol a été supprimé, false s'il n'existe pas.
     */
    public boolean deleteOneById(String id) {
        Flight flight = em.find(Flight.class, id);
        boolean exists = flight != null;
        if (exists) {
            deleteBaggageByFlightId(id); // cascade
            em.remove(flight);
        }
        return exists;
    }

    /**
     * Supprime tous les bagages rattachés à un vol.
     *
     * @param flightId l'id du vol ciblé.
     */
    private void deleteBaggageByFlightId(String flightId) {
        var dq = em.createQuery("SELECT b FROM Baggage b WHERE b.flight.id = :vId", Baggage.class);
        dq.setParameter("vId", flightId);
        for (Baggage b : dq.getResultList()) {
            em.remove(b);
        }
    }

}
