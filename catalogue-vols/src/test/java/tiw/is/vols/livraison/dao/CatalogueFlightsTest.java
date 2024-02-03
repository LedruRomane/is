/*
package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.*;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueFlightsTest extends CatalogueTest {

*
     * Testing EntityManager setup


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

*
     * Testing listing of flights

    @Test
    void getVols() {
        Collection<Flight> cv = catalogueVol.getVols();
        for (Flight v : flights) {
            assertTrue(cv.contains(v), () -> (v + " not in " + cv));
        }
    }


@Test
    void getVol() {
        assertEquals(flights[0], catalogueVol.getVol(flights[0].getId()));
        assertNull(catalogueVol.getVol("v-"+testName+": je n'existe pas"));
    }


*
     * Test de sauvegarde d'un vol en base


    @Test
    void saveVol() {
        Company c = companies[0];
        Flight v = new Flight("vol-in-"+testName, c, "e");
        em.getTransaction().begin();
        assertEquals(v, catalogueVol.saveVol(v));
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
        boolean deleted = catalogueVol.deleteVolById(v.getId());
        em.getTransaction().commit();
        assertTrue(deleted);
        em.getTransaction().begin();
        deleted = catalogueVol.deleteVolById(v.getId());
        em.getTransaction().commit();
        assertFalse(deleted);
    }
}
*/
