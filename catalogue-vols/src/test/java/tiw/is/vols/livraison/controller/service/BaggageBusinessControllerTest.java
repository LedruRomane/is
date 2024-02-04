/*
package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BaggageBusinessControllerTest extends CatalogueTest {
    private BagageBusinessController controller;
    private final BaggageDTO dumbBagage = new BaggageDTO(
            "Crazy flying saucer",
            0 */
/* Inutilisé pour la création *//*
,
            -10 */
/* (c'est de l'antimatière) *//*
,
            "E.T.");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new BagageBusinessController(catalogueBagage);
    }

    @Test
    void delivrer() {
        try {
            controller.delivrer(BaggageDTO.fromBaggage(baggages[0]));
            assertTrue(baggages[0].isDelivre());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.delivrer(dumbBagage));
    }

    @Test
    void recuperer() {
        try {
            baggages[0].delivrer();
            controller.recuperer(BaggageDTO.fromBaggage(baggages[0]));
            assertTrue(baggages[0].isRecupere());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () -> controller.recuperer(BaggageDTO.fromBaggage(baggages[1])));
        assertThrows(ResourceNotFoundException.class, () -> controller.recuperer(dumbBagage));
    }
}*/
