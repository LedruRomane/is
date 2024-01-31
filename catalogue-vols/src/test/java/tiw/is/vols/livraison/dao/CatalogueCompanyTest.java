package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Company;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueCompanyTest extends CatalogueTest {

    @Test
    void getCompanies() {
        var comps = catalogCompany.getCompanies();
        for (Company c : companies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    void getCompany() {
        assertEquals(companies[0],
                catalogCompany.getCompany(companies[0].getId()));
        assertNull(catalogCompany.getCompany("je n'existe pas"));
    }

    @Test
    void saveCompany() {
        var c = new Company("c-" + testName + "-new");
        try {
            em.getTransaction().begin();
            var c2 = catalogCompany.saveCompany(c);
            assertNotNull(c2);
            em.getTransaction().commit();
            assertEquals(c, em.find(Company.class, c.getId()));
        } finally {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            var c3 = em.find(Company.class, c.getId());
            if (c3 != null) {
                em.remove(c3);
            }
            em.getTransaction().commit();
        }
    }

    @Test
    void deleteCompanyById() {
        String id = companies[2].getId();
        em.getTransaction().begin();
        var d = catalogCompany.deleteCompanyById(id);
        em.getTransaction().commit();
        assertTrue(d);
        em.getTransaction().begin();
        assertFalse(catalogCompany.deleteCompanyById(id));
        em.getTransaction().commit();
    }
}