package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BaggageDaoTest extends DataAccessObjectTest {

    private Collection<Baggage> getBagageByVolId(String volId) {
        var dq = em.createQuery(
                "SELECT b FROM Baggage b WHERE b.flight.id = :vId",
                Baggage.class);
        dq.setParameter("vId", volId);
        return dq.getResultList();
    }

    @Test
    void getBagages() {
        var bags = baggageDao.getAll();
        for (Baggage b : baggages) {
            assertTrue(bags.contains(b));
        }
    }

    @Test
    void createBagage() {
        Flight v = flights[0];
        em.getTransaction().begin();
        String passenger = "p-" + testName + "nouveau";
        Baggage b = baggageDao.save(v.createBagage(2, passenger));
        em.getTransaction().commit();
        assertNotNull(b);
        assertEquals(2, b.getWeight());
        assertEquals(passenger, b.getPassenger());
        em.getTransaction().begin();
        Baggage b2 = baggageDao.save(v.createBagage(2, passenger));
        em.getTransaction().commit();
        assertNotEquals(b, b2);
        assertNotEquals(b.getNumero(),b2.getNumero());
        em.getTransaction().begin();
        em.remove(b);
        em.remove(b2);
        em.getTransaction().commit();
    }

    @Test
    void getBagageById() {
        Baggage b = baggages[0];
        assertNotNull(baggageDao.getOneById(b.getFlight().getId(), b.getNumero()));
        assertNull(baggageDao.getOneById("vol inexistant", 1));
        assertNull(baggageDao.getOneById(b.getFlight().getId(), -1));
    }

    @Test
    void deleteBagageById() {
        Baggage b = baggages[2];
        em.getTransaction().begin();
        assertTrue(baggageDao.deleteOneById(b.getFlight().getId(), b.getNumero()));
        em.getTransaction().commit();
        em.getTransaction().begin();
        assertFalse(baggageDao.deleteOneById(b.getFlight().getId(), b.getNumero()));
        em.getTransaction().commit();
    }

    @Test
    void getBagagesPerdusByVolId() {
        String lastVolId = flights[flights.length-1].getId();
        Collection<Baggage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Baggage::isDelivre), "Il n'y a pas de bagage délivré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isDelivre()),
                "Il n'y a pas de bagage non délivré");
        var bgg = baggageDao.getBagagesPerdusByFlightId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Baggage b:bgg) {
            assertFalse(b.isDelivre(),
                    "Le bagage "+b.getFlight()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les baggages perdus");
        }
    }

    @Test
    void getBagagesNonRecuperesByVolId() {
        String lastVolId = flights[flights.length-1].getId();
        Collection<Baggage> bagageByVolId = getBagageByVolId(lastVolId);
        assertTrue(bagageByVolId.stream().anyMatch(Baggage::isRecupere), "Il " +
                "n'y a pas de bagage récupéré");
        assertTrue(bagageByVolId.stream().anyMatch(b -> !b.isRecupere()),
                "Il n'y a pas de bagage non récupéré");
        var bgg = baggageDao.getBagagesNonRecuperesByFlightId(lastVolId);
        assertTrue(bgg.size() > 0);
        for(Baggage b:bgg) {
            assertFalse(b.isRecupere(),
                    "Le bagage "+b.getFlight()+"-"+b.getNumero()+" ne devrait " +
                            "pas figurer dans les baggages non recuperes");
        }
    }
}
