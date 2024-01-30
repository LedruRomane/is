package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.*;
import tiw.is.vols.livraison.model.Compagnie;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueVolTest extends CatalogueTest {

    /**
     * Testing EntityManager setup
     */
    @Test
    void testEMSetup() {
        Compagnie c = new Compagnie("c-testEMSetup");
        Vol v = new Vol("vol-in-testEMSetup", c, "a");
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class, v.getId());
        assertEquals(v, v2);
        em.remove(v2);
        em.remove(em.find(Compagnie.class, c.getId()));
        em.getTransaction().commit();
    }

    /**
     * Testing listing of vols
     */
    @Test
    void getVols() {
        Collection<Vol> cv = catalogueVol.getVols();
        for (Vol v : vols) {
            assertTrue(cv.contains(v), () -> (v + " not in " + cv));
        }
    }

    @Test
    void getVol() {
        assertEquals(vols[0], catalogueVol.getVol(vols[0].getId()));
        assertNull(catalogueVol.getVol("v-"+testName+": je n'existe pas"));
    }

    /**
     * Test de sauvegarde d'un vol en base
     */
    @Test
    void saveVol() {
        Compagnie c = compagnies[0];
        Vol v = new Vol("vol-in-"+testName, c, "e");
        em.getTransaction().begin();
        assertEquals(v, catalogueVol.saveVol(v));
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class,v.getId());
        assertNotNull(v2);
        em.remove(v2);
        em.getTransaction().commit();
    }

    @Test
    void deleteVolById() {
        em.getTransaction().begin();
        Vol v = vols[0];
        boolean deleted = catalogueVol.deleteVolById(v.getId());
        em.getTransaction().commit();
        assertTrue(deleted);
        em.getTransaction().begin();
        deleted = catalogueVol.deleteVolById(v.getId());
        em.getTransaction().commit();
        assertFalse(deleted);
    }
}
