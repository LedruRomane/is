package tiw.is.vols.livraison.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.dao.CatalogueVol;
import tiw.is.vols.livraison.model.Compagnie;
import tiw.is.vols.livraison.model.Vol;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceManagerTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setupClass() {
        emf = PersistenceManager.createEntityManagerFactory();
    }

    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    /**
     * Testing EntityManager setup
     */
    @Test
    public void testEMSetup() {
        EntityManager em = emf.createEntityManager();
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
    public void testListVols() {
        EntityManager em = emf.createEntityManager();
        Compagnie c = new Compagnie("c-testListVols");
        Vol v1 = new Vol("vol-in-testListVols1",c, "b");
        Vol v2 = new Vol("vol-in-testListVols2",c, "c");
        Vol v3 = new Vol("vol-in-testListVols3",c, "d");
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v1);
        em.persist(v2);
        em.persist(v3);
        em.getTransaction().commit();
        CatalogueVol cat = new CatalogueVol(em);
        Collection<Vol> cv = cat.getVols();
        for (Vol v : Arrays.asList(v1, v2, v3)) {
            assertTrue(cv.contains(v), () -> (v + " not in " + cv));
        }
        em.getTransaction().begin();
        for (Vol v : Arrays.asList(v1, v2, v3)) {
            em.remove(em.find(Vol.class, v.getId()));
        }
        em.remove(c);
        em.getTransaction().commit();
    }

    /**
     * Test de sauvegarde d'un vol en base
     */
    @Test
    public void testSaveVol() {
        EntityManager em = emf.createEntityManager();
        Compagnie c = new Compagnie("c-testSaveVol");
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        CatalogueVol cat = new CatalogueVol(em);
        Vol v = new Vol("vol-in-testSaveVol", c, "e");
        em.getTransaction().begin();
        cat.saveVol(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class,v.getId());
        assertNotNull(v2);
        em.remove(v2);
        em.remove(em.find(Compagnie.class, c.getId()));
        em.getTransaction().commit();
    }
}
