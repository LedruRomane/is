package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BagageBusinessControllerTest extends CatalogueTest {
    private BagageBusinessController controller;
    private final BagageDTO dumbBagage = new BagageDTO(
            "Crazy flying saucer",
            0 /* Inutilisé pour la création */,
            -10 /* (c'est de l'antimatière) */,
            "E.T.");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new BagageBusinessController(catalogueBagage);
    }

    @Test
    void delivrer() {
        try {
            controller.delivrer(BagageDTO.fromBagage(bagages[0]));
            assertTrue(bagages[0].isDelivre());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.delivrer(dumbBagage));
    }

    @Test
    void recuperer() {
        try {
            bagages[0].delivrer();
            controller.recuperer(BagageDTO.fromBagage(bagages[0]));
            assertTrue(bagages[0].isRecupere());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () -> controller.recuperer(BagageDTO.fromBagage(bagages[1])));
        assertThrows(ResourceNotFoundException.class, () -> controller.recuperer(dumbBagage));
    }
}