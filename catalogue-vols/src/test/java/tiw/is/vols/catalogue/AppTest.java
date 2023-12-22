package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.catalogue.db.PersistenceManager;
import tiw.is.vols.catalogue.modeles.Companie;
import tiw.is.vols.catalogue.modeles.Vol;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

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
        Companie c = new Companie("c-testEMSetup");
        Vol v = new Vol("vol-in-testEMSetup", c);
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class, v.getId());
        assertEquals(v, v2);
        em.remove(v2);
        em.remove(em.find(Companie.class, c.getId()));
        em.getTransaction().commit();
    }

    /**
     * Testing listing of vols
     */
    @Test
    public void testListVols() {
        EntityManager em = emf.createEntityManager();
        Companie c = new Companie("c-testListVols");
        Vol v1 = new Vol("vol-in-testListVols1",c);
        Vol v2 = new Vol("vol-in-testListVols2",c);
        Vol v3 = new Vol("vol-in-testListVols3",c);
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v1);
        em.persist(v2);
        em.persist(v3);
        em.getTransaction().commit();
        Catalogue cat = new Catalogue(em);
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
        Companie c = new Companie("c-testSaveVol");
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        Catalogue cat = new Catalogue(em);
        Vol v = new Vol("vol-in-testSaveVol", c);
        em.getTransaction().begin();
        cat.saveVol(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class,v.getId());
        assertNotNull(v2);
        em.remove(v2);
        em.remove(em.find(Companie.class, c.getId()));
        em.getTransaction().commit();
    }
}
