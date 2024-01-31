package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Bagage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class BagageOperationControllerTest extends CatalogueTest {
    private BagageOperationController controller;
    private final BagageDTO dumbBagage = new BagageDTO(
            "Crazy flying saucer",
            0 /* Inutilisé pour la création */,
            -10 /* (c'est de l'antimatière) */,
            "E.T.");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new BagageOperationController(catalogueBagage, catalogueVol);
    }

    @Test
    public void getBagages() {
        Collection<BagageDTO> bagages1 = controller.getBagages();
        for(BagageDTO b: bagages1) {
            assertTrue(Arrays.stream(bagages).toList().contains(new Bagage(catalogueVol.getVol(b.volId()), b.numero(), b.poids(), b.passager())));
        }
        for(Bagage b: bagages) {
            assertTrue(bagages1.contains(BagageDTO.fromBagage(b)));
        }
    }

    @Test
    public void getBagage() {
        try {
            assertEquals(BagageDTO.fromBagage(bagages[0]), controller.getBagage(BagageDTO.fromBagage(bagages[0])));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.getBagage(dumbBagage));
    }

    @Test
    public void createBagage() {
        try {
            catalogueVol.saveVol(new Vol(dumbBagage.volId(), catalogCompany.saveCompany(new Company("ALF Inc.")), "Jupiter"));
            BagageDTO res = controller.createBagage(dumbBagage);
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
        assertDoesNotThrow(() -> controller.deleteBagage(BagageDTO.fromBagage(bagages[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteBagage(dumbBagage));
    }
}
