package tiw.is.vols.livraison.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceManagerTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setupClass() {
        PersistenceManager pm = new PersistenceManager(
                "localhost",
                "catalogue-db",
                "catalogue",
                "catalogue-mdp"
        );
        emf = pm.createEntityManagerFactory();
    }

    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }


    @Test
    public void testEMSetup() {
        EntityManager em = emf.createEntityManager();
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
    public void testListVols() {
        EntityManager em = emf.createEntityManager();
        Company c = new Company("c-testListVols");
        Flight v1 = new Flight("vol-in-testListVols1",c, "b");
        Flight v2 = new Flight("vol-in-testListVols2",c, "c");
        Flight v3 = new Flight("vol-in-testListVols3",c, "d");
        em.getTransaction().begin();
        em.persist(c);
        em.persist(v1);
        em.persist(v2);
        em.persist(v3);
        em.getTransaction().commit();
        FlightDao cat = new FlightDao(em);
        Collection<Flight> cv = cat.getAll();
        for (Flight v : Arrays.asList(v1, v2, v3)) {
            assertTrue(cv.contains(v), () -> (v + " not in " + cv));
        }
        em.getTransaction().begin();
        for (Flight v : Arrays.asList(v1, v2, v3)) {
            em.remove(em.find(Flight.class, v.getId()));
        }
        em.remove(c);
        em.getTransaction().commit();
    }

    @Test
    public void testSaveVol() {
        EntityManager em = emf.createEntityManager();
        Company c = new Company("c-testSaveVol");
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        FlightDao cat = new FlightDao(em);
        Flight v = new Flight("vol-in-testSaveVol", c, "e");
        em.getTransaction().begin();
        cat.save(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Flight v2 = em.find(Flight.class,v.getId());
        assertNotNull(v2);
        em.remove(v2);
        em.remove(em.find(Company.class, c.getId()));
        em.getTransaction().commit();
    }
}
