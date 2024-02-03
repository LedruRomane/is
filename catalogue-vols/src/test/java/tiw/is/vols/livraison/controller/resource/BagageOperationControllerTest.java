/*
package tiw.is.flights.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.flights.livraison.dao.CatalogueTest;
import tiw.is.flights.livraison.dto.BaggageDTO;
import tiw.is.flights.livraison.exception.ResourceNotFoundException;
import tiw.is.flights.livraison.model.Baggage;
import tiw.is.flights.livraison.model.Company;
import tiw.is.flights.livraison.model.Flight;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class BagageOperationControllerTest extends CatalogueTest {
    private BagageOperationController controller;
    private final BaggageDTO dumbBagage = new BaggageDTO(
            "Crazy flying saucer",
            0
 Inutilisé pour la création
,
            -10
 (c'est de l'antimatière)
,
            "E.T.");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new BagageOperationController(catalogueBagage, catalogueVol);
    }

@Test
    public void getBagages() {
        Collection<BaggageDTO> bagages1 = controller.getBagages();
        for(BaggageDTO b: bagages1) {
            assertTrue(Arrays.stream(baggages).toList().contains(new Baggage(catalogueVol.getVol(b.volId()), b.numero(), b.weight(), b.passenger())));
        }
        for(Baggage b: baggages) {
            assertTrue(bagages1.contains(BaggageDTO.fromBaggage(b)));
        }
    }


    @Test
    public void getBagage() {
        try {
            assertEquals(BaggageDTO.fromBaggage(baggages[0]), controller.getBagage(BaggageDTO.fromBaggage(baggages[0])));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.getBagage(dumbBagage));
    }

    @Test
    public void createBagage() {
        try {
            catalogueVol.saveVol(new Flight(dumbBagage.volId(), catalogCompany.saveCompany(new Company("ALF Inc.")), "Jupiter"));
            BaggageDTO res = controller.createBagage(dumbBagage);
            assertNotNull(catalogueBagage.getBagageById(dumbBagage.volId(), res.numero()));
            assertEquals(dumbBagage.volId(), res.volId());
            assertEquals(dumbBagage.weight(), res.weight());
            assertEquals(dumbBagage.passenger(), res.passenger());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteBagage() {
        assertDoesNotThrow(() -> controller.deleteBagage(BaggageDTO.fromBaggage(baggages[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteBagage(dumbBagage));
    }
}
*/
