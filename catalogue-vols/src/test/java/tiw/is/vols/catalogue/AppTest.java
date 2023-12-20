package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.catalogue.modeles.Vol;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setupClass() {
        Map<String, Object> config = new HashMap<>();
        config.put("hibernate.hbm2ddl.auto", "update");
        config.put("jakarta.persistence.jdbc.url", "jdbc:postgresql:catalogue-db");
        config.put("jakarta.persistence.jdbc.user", "catalogue");
        config.put("jakarta.persistence.jdbc.password", "catalogue-mdp");
        config.put("hibernate.dialect", org.hibernate.dialect.PostgreSQLDialect.class.getCanonicalName());
        config.put("hibernate.hbm2ddl.auto", "create");
        emf = Persistence
                .createEntityManagerFactory("pu-catalogue", config);
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
        Vol v = new Vol("vol-in-testEMSetup");
        em.getTransaction().begin();
        em.persist(v);
        em.getTransaction().commit();
        em.getTransaction().begin();
        Vol v2 = em.find(Vol.class, v.getId());
        assertEquals(v, v2);
        em.remove(v2);
        em.getTransaction().commit();
    }

    /**
     * Testing listing of vols
     */
    @Test
    public void testListVols() {
        EntityManager em = emf.createEntityManager();
        Vol v1 = new Vol("vol-in-testListVols1");
        Vol v2 = new Vol("vol-in-testListVols2");
        Vol v3 = new Vol("vol-in-testListVols3");
        em.getTransaction().begin();
        em.persist(v1);
        em.persist(v2);
        em.persist(v3);
        em.getTransaction().commit();
        Catalogue c = new Catalogue(emf);
        Collection<Vol> cv = c.getVols();
        for(Vol v: Arrays.asList(v1,v2,v3)) {
            assertTrue(cv.contains(v), () -> (v+" not in "+cv));
        }
        em.getTransaction().begin();
        for(Vol v: Arrays.asList(v1,v2,v3)) {
            em.remove(em.find(Vol.class, v.getId()));
        }
        em.getTransaction().commit();
    }
}
