package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.catalogue.db.PersistenceManager;
import tiw.is.vols.catalogue.modeles.Bagage;
import tiw.is.vols.catalogue.modeles.BagageKey;
import tiw.is.vols.catalogue.modeles.Companie;
import tiw.is.vols.catalogue.modeles.Vol;

import static org.junit.jupiter.api.Assertions.*;

public class BagageTest {
    private static EntityManagerFactory emf;

    public static void loadFixtures() {
        EntityManager em = emf.createEntityManager();

        Companie c1 = new Companie("Compagnie A");
        Vol v1 = new Vol("vol1",c1, true, "b");
        BagageKey b1Key = new BagageKey(v1, 1);
        Bagage b1 = new Bagage(b1Key, 10, "passager1");

        em.getTransaction().begin();
        em.persist(c1);
        em.persist(v1);
        em.persist(b1);
        em.getTransaction().commit();
    }

    public static void pruneFixtures() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(Bagage.class, new BagageKey(em.find(Vol.class, "vol1"), 1)));
        em.remove(em.find(Vol.class, "vol1"));
        em.remove(em.find(Companie.class, "Compagnie A"));
        em.getTransaction().commit();
    }

    @BeforeAll
    public static void setupClass() {
        emf = PersistenceManager.createEntityManagerFactory();
        loadFixtures();
    }
    @AfterAll
    public static void tearDownClass() {
        pruneFixtures();
        emf.close();
    }

    @Test
    public void testGetBagageById() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        Bagage b = c.getBagageById("vol1", 1);
        assertEquals(10, b.getPoids());
        assertEquals("passager1", b.getPassagerRef());
    }

    @Test
    public void testdeleteBagageById() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        c.deleteBagageById("vol1", 1);
        Bagage b = c.getBagageById("vol1", 1);
        assertNull(b);
    }

    @Test
    public void testCreateBagage() {
        Catalogue c = new Catalogue(emf.createEntityManager());

        BagageKey b2Key = new BagageKey(c.getVol("vol1"), 2);
        c.createBagage(b2Key, 20, "passager2");

        Bagage b = c.getBagageById("vol1", 2);
        assertEquals(20, b.getPoids());
        assertEquals("passager2", b.getPassagerRef());
    }
}
