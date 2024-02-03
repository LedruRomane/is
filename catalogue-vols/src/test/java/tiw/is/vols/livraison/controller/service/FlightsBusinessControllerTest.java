package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.CatalogueTest;
import tiw.is.vols.livraison.dto.FlightDTO;

class FlightsBusinessControllerTest extends CatalogueTest {
    private VolBusinessController controller;
    private final FlightDTO dumbVol = new FlightDTO("UFO Mogul", "USAF", "Roswell");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new VolBusinessController(catalogueVol, catalogueBagage);
    }

/*

    @Test
    void fermerLivraison() {
        assertDoesNotThrow(() -> controller.fermerLivraison(FlightDTO.fromVol(flights[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.fermerLivraison(dumbVol));
    }
*/

    /*@Test
    void bagagesPerdus() {
        flights[1].fermerLivraison();
        try {
            Collection<BaggageDTO> bagageDTOS = controller.bagagesPerdus(
                    FlightDTO.fromVol(flights[1]));
            assertEquals(1,
                    bagageDTOS.size());
            var bDto = bagageDTOS.stream().findFirst().get();
            em.getTransaction().begin();
            catalogueBagage.getBagageById(flights[1].getId(), bDto.numero()).delivrer();
            em.getTransaction().commit();
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        flights[1].fermerLivraison();
        try {
            assertEquals(0,
                    controller.bagagesPerdus(FlightDTO.fromVol(flights[1])).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class,
                () -> controller.bagagesPerdus(FlightDTO.fromVol(flights[3])));
        assertThrows(ResourceNotFoundException.class, () -> controller.bagagesPerdus(dumbVol));
    }*/

    /*@Test
    void bagagesNonRecuperes() {
        Flight vol = flights[1];
        vol.fermerLivraison();
        try {
            Collection<BaggageDTO> bagageDTOS = controller.bagagesNonRecuperes(
                    FlightDTO.fromVol(vol));
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
                    controller.bagagesNonRecuperes(FlightDTO.fromVol(vol)).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () -> controller.bagagesNonRecuperes(FlightDTO.fromVol(flights[2])));
        assertThrows(ResourceNotFoundException.class, () -> controller.bagagesPerdus(dumbVol));
    }*/
}