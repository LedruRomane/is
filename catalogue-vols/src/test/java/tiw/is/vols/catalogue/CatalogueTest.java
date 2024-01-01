package tiw.is.vols.catalogue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tiw.is.vols.catalogue.db.PersistenceManager;
import tiw.is.vols.catalogue.modeles.Companie;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class CatalogueTest {
    private static EntityManagerFactory emf;

    public static void loadFixtures() {
        EntityManager em = emf.createEntityManager();
        Companie c1 = new Companie("Compagnie A");
        Companie c2 = new Companie("Compagnie B");
        em.getTransaction().begin();
        em.persist(c1);
        em.persist(c2);
        em.getTransaction().commit();
    }

    @BeforeAll
    public static void setupClass() {
        emf = PersistenceManager.createEntityManagerFactory();
        loadFixtures();
    }
    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    /**
     * Testing getCompanies.
     */
    @Test
    public void testGetCompanies() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        Collection<Companie> companies = c.getCompanies();
        assertEquals(2, companies.size());
    }

    /**
     * Testing getCompany.
     */
    @Test
    public void testGetCompany() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        Companie company = c.getCompany("Compagnie A");
        assertEquals("Compagnie A", company.getId());
    }

    /**
     * Testing saveCompany.
     */
    @Test
    public void testSaveCompany() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        Companie company = new Companie("Compagnie C");
        c.saveCompany(company);
        Companie company2 = c.getCompany("Compagnie C");
        assertEquals(company, company2);
    }

    /**
     * Testing deleteCompanyById.
     */
    @Test
    public void testDeleteCompanyById() {
        Catalogue c = new Catalogue(emf.createEntityManager());
        c.deleteCompanyById("Compagnie A");
        assertNull(c.getCompany("Compagnie A"));
    }

}
