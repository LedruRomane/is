package tiw.is.vols.livraison.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tiw.is.vols.livraison.model.Company;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoTest  extends DataAccessObjectTest {

    @Test
    void getCompanies() {
        var comps = companyDao.getAll();
        for (Company c : companies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    void getCompany() {
        assertEquals(companies[0], companyDao.getOneById(companies[0].getId()));
        assertNull(companyDao.getOneById("je n'existe pas"));
    }

    @Test
    void saveCompany() {
        var c = new Company("c-" + testName + "-new");
        try {
            em.getTransaction().begin();
            var c2 = companyDao.save(c);
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
        var d = companyDao.deleteOneById(id);
        em.getTransaction().commit();
        Assertions.assertTrue(d);
        em.getTransaction().begin();
        assertFalse(companyDao.deleteOneById(id));
        em.getTransaction().commit();
    }
}
