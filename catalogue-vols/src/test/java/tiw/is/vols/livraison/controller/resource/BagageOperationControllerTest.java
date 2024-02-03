/*
package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

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
            assertTrue(Arrays.stream(baggages).toList().contains(new Baggage(catalogueVol.getVol(b.volId()), b.numero(), b.poids(), b.passager())));
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
            catalogueVol.saveVol(new Vol(dumbBagage.volId(), catalogCompany.saveCompany(new Company("ALF Inc.")), "Jupiter"));
            BaggageDTO res = controller.createBagage(dumbBagage);
            assertNotNull(catalogueBagage.getBagageById(dumbBagage.volId(), res.numero()));
            assertEquals(dumbBagage.volId(), res.volId());
            assertEquals(dumbBagage.poids(), res.poids());
            assertEquals(dumbBagage.passager(), res.passager());
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
