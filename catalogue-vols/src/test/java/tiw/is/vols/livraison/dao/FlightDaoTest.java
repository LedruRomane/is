package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FlightDaoTest extends DataAccessObjectTest {

    @Test
    void testEMSetup() {
        Company c = new Company("c-testEMSetup");
        Flight v = new Flight("vol-in-testEMSetup", c, "a");
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Flight v2 = em.find(Flight.class, v.getId());
        assertEquals(v, v2);
        em.remove(v2);
        em.remove(em.find(Company.class, c.getId()));
        em.getTransaction().commit();
    }

    @Test
    void getVols() {
        Collection<Flight> cv = flightDao.getAll();
        for (Flight v : flights) {
            assertTrue(cv.contains(v), () -> (v + " not in " + cv));
        }
    }

    @Test
    void getVol() {
        assertEquals(flights[0], flightDao.getOneById(flights[0].getId()));
        assertNull(flightDao.getOneById("v-"+testName+": je n'existe pas"));
    }

    @Test
    void saveVol() {
        Company c = companies[0];
        Flight v = new Flight("vol-in-"+testName, c, "e");
        em.getTransaction().begin();
        assertEquals(v, flightDao.save(v));
        em.getTransaction().commit();
        em.getTransaction().begin();
        Flight v2 = em.find(Flight.class,v.getId());
        assertNotNull(v2);
        em.remove(v2);
        em.getTransaction().commit();
    }

    @Test
    void deleteVolById() {
        em.getTransaction().begin();
        Flight v = flights[0];
        boolean deleted = flightDao.deleteOneById(v.getId());
        em.getTransaction().commit();
        assertTrue(deleted);
        em.getTransaction().begin();
        deleted = flightDao.deleteOneById(v.getId());
        em.getTransaction().commit();
        assertFalse(deleted);
    }
}
