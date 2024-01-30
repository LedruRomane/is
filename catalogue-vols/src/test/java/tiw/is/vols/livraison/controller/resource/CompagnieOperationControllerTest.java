package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Compagnie;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CompagnieOperationControllerTest extends CatalogueTest {
    private CompagnieOperationController controller;
    private final Compagnie dumbCompagnie = new Compagnie("Mon beau navion");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new CompagnieOperationController(catalogueCompanie);
    }

    @Test
    public void getCompagnies() {
        Collection<Compagnie> comps = controller.getCompagnies();
        for(Compagnie c: comps) {
            assertTrue(Arrays.stream(compagnies).toList().contains(c));
        }
        for(Compagnie c: compagnies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    public void getCompagnie() {
        try {
            assertEquals(compagnies[0], controller.getCompagnie(compagnies[0].getId()));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.getCompagnie(dumbCompagnie.getId()));
    }

    @Test
    public void createCompagnie() {
        try {
            controller.createCompagnie(dumbCompagnie);
            assertNotNull(catalogueCompanie.getCompagnie(dumbCompagnie.getId()));
        } catch (ResourceAlreadyExistsException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceAlreadyExistsException.class, () -> controller.createCompagnie(compagnies[0]));
    }

    @Test
    public void deleteCompagnie() {
        assertDoesNotThrow(() -> controller.deleteCompagnie(compagnies[0]));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteCompagnie(dumbCompagnie));
    }
}