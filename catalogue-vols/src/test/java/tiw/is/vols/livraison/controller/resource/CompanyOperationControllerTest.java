/*
package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Company;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyOperationControllerTest extends CatalogueTest {
    private CompanyOperationController controller;
    private final Company dumbCompany = new Company("Mon beau navion");
    private final Company anotherCompany = new Company("nothing");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new CompanyOperationController(catalogCompany);
    }

    @Test
    public void getCompanies() {
        Collection<Company> comps = controller.getCompanies();
        for(Company c: comps) {
            assertTrue(Arrays.stream(companies).toList().contains(c));
        }
        for(Company c: companies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    public void getCompany() {
        try {
            assertEquals(companies[0], controller.getCompany(companies[0].getId()));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.getCompany(anotherCompany.getId()));
    }

    @Test
    public void createCompany() {
        try {
            controller.createCompany(dumbCompany);
            assertNotNull(catalogCompany.getCompany(dumbCompany.getId()));
        } catch (ResourceAlreadyExistsException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceAlreadyExistsException.class, () -> controller.createCompany(companies[0]));
    }

    @Test
    public void deleteCompany() {
        assertDoesNotThrow(() -> controller.deleteCompany(companies[0]));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteCompany(anotherCompany));
    }
}*/
