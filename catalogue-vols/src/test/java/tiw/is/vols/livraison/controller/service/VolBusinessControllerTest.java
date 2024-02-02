package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.BagageDTO;
import tiw.is.vols.livraison.dto.VolDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.model.Vol;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class VolBusinessControllerTest extends CatalogueTest {
    private VolBusinessController controller;
    private final VolDTO dumbVol = new VolDTO("UFO Mogul", "USAF", "Roswell");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new VolBusinessController(catalogueVol, catalogueBagage);
    }

/*

    @Test
    void fermerLivraison() {
        assertDoesNotThrow(() -> controller.fermerLivraison(VolDTO.fromVol(vols[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.fermerLivraison(dumbVol));
    }
*/

    /*@Test
    void bagagesPerdus() {
        vols[1].fermerLivraison();
        try {
            Collection<BagageDTO> bagageDTOS = controller.bagagesPerdus(
                    VolDTO.fromVol(vols[1]));
            assertEquals(1,
                    bagageDTOS.size());
            var bDto = bagageDTOS.stream().findFirst().get();
            em.getTransaction().begin();
            catalogueBagage.getBagageById(vols[1].getId(), bDto.numero()).delivrer();
            em.getTransaction().commit();
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        vols[1].fermerLivraison();
        try {
            assertEquals(0,
                    controller.bagagesPerdus(VolDTO.fromVol(vols[1])).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class,
                () -> controller.bagagesPerdus(VolDTO.fromVol(vols[3])));
        assertThrows(ResourceNotFoundException.class, () -> controller.bagagesPerdus(dumbVol));
    }*/

    /*@Test
    void bagagesNonRecuperes() {
        Vol vol = vols[1];
        vol.fermerLivraison();
        try {
            Collection<BagageDTO> bagageDTOS = controller.bagagesNonRecuperes(
                    VolDTO.fromVol(vol));
            assertEquals(2, bagageDTOS.size());
            em.getTransaction().begin();
            for (var bDto: bagageDTOS) {
                var b = catalogueBagage.getBagageById(bDto.volId(), bDto.numero());
                b.delivrer();
                b.recuperer();
            }
            em.getTransaction().commit();
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        vol.fermerLivraison();
        try {
            assertEquals(0,
                    controller.bagagesNonRecuperes(VolDTO.fromVol(vol)).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () -> controller.bagagesNonRecuperes(VolDTO.fromVol(vols[2])));
        assertThrows(ResourceNotFoundException.class, () -> controller.bagagesPerdus(dumbVol));
    }*/
}