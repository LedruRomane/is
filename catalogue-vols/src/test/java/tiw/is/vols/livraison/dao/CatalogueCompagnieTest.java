package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Compagnie;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueCompagnieTest extends CatalogueTest {

    @Test
    void getCompagnies() {
        var comps = catalogueCompanie.getCompagnies();
        for (Compagnie c : compagnies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    void getCompagnie() {
        assertEquals(compagnies[0],
                catalogueCompanie.getCompagnie(compagnies[0].getId()));
        assertNull(catalogueCompanie.getCompagnie("je n'existe pas"));
    }

    @Test
    void saveCompagnie() {
        var c = new Compagnie("c-" + testName + "-new");
        try {
            em.getTransaction().begin();
            var c2 = catalogueCompanie.saveCompagnie(c);
            assertNotNull(c2);
            em.getTransaction().commit();
            assertEquals(c, em.find(Compagnie.class, c.getId()));
        } finally {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            var c3 = em.find(Compagnie.class, c.getId());
            if (c3 != null) {
                em.remove(c3);
            }
            em.getTransaction().commit();
        }
    }

    @Test
    void deleteCompagnieById() {
        String id = compagnies[2].getId();
        em.getTransaction().begin();
        var d = catalogueCompanie.deleteCompagnieById(id);
        em.getTransaction().commit();
        assertTrue(d);
        em.getTransaction().begin();
        assertFalse(catalogueCompanie.deleteCompagnieById(id));
        em.getTransaction().commit();
    }
}