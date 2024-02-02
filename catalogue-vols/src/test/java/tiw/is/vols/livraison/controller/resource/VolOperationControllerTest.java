/*
package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Vol;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class VolOperationControllerTest extends CatalogueTest {
    private VolOperationController controller;
    private final VolDTO dumbVol = new VolDTO("UFO Mogul", "USAF", "Roswell");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new VolOperationController(catalogueVol, catalogCompany);
    }

@Test
    public void getVols() {
        Collection<VolDTO> vols1 = controller.getVols();
        for(VolDTO v: vols1) {
            assertTrue(Arrays.stream(vols).toList().contains(new Vol(v.id(), catalogCompany.getCompany(v.company()), v.pointLivraisonBagages())));
        }
        for(Vol v: vols) {
            assertTrue(vols1.contains(VolDTO.fromVol(v)));
        }
    }


@Test
    public void getVol() {
        try {
            assertEquals(VolDTO.fromVol(vols[0]), controller.getVol(VolDTO.fromVol(vols[0])));
        } catch (ResourceNotFoundException e) {
            fail();
        }
        assertThrows(ResourceNotFoundException.class, () -> controller.getVol(dumbVol));
    }


    @Test
    public void createVol() {
        catalogCompany.saveCompany(new Company(dumbVol.company()));
        try {
            controller.updateVol(dumbVol);
            assertNotNull(catalogueVol.getVol(dumbVol.id()));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateVol() {
        String nouveauPointLivraison = "Pétaouchnok";
        VolDTO dto = new VolDTO(vols[0].getId(), companies[0].getId(), nouveauPointLivraison);
        try {
            controller.updateVol(dto);
            assertEquals(catalogueVol.getVol(vols[0].getId()).getPointLivraisonBagages(), nouveauPointLivraison);
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

@Test
    public void deleteVol() {
        assertDoesNotThrow(() -> controller.deleteVol(VolDTO.fromVol(vols[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteVol(dumbVol));
    }

}
*/
