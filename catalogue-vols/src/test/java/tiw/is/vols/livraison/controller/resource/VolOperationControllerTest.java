/*
package tiw.is.flights.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.flights.livraison.dao.CatalogueTest;
import tiw.is.flights.livraison.dto.FlightDTO;
import tiw.is.flights.livraison.exception.ResourceNotFoundException;
import tiw.is.flights.livraison.model.Company;
import tiw.is.flights.livraison.model.Flight;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class VolOperationControllerTest extends CatalogueTest {
    private VolOperationController controller;
    private final FlightDTO dumbVol = new FlightDTO("UFO Mogul", "USAF", "Roswell");
    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        controller = new VolOperationController(catalogueVol, catalogCompany);
    }

@Test
    public void getVols() {
        Collection<FlightDTO> vols1 = controller.getVols();
        for(FlightDTO v: vols1) {
            assertTrue(Arrays.stream(flights).toList().contains(new Flight(v.id(), catalogCompany.getCompany(v.company()), v.pointLivraisonBagages())));
        }
        for(Flight v: flights) {
            assertTrue(vols1.contains(FlightDTO.fromVol(v)));
        }
    }


@Test
    public void getVol() {
        try {
            assertEquals(FlightDTO.fromVol(flights[0]), controller.getVol(FlightDTO.fromVol(flights[0])));
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
        FlightDTO dto = new FlightDTO(flights[0].getId(), companies[0].getId(), nouveauPointLivraison);
        try {
            controller.updateVol(dto);
            assertEquals(catalogueVol.getVol(flights[0].getId()).getPointLivraisonBagages(), nouveauPointLivraison);
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

@Test
    public void deleteVol() {
        assertDoesNotThrow(() -> controller.deleteVol(FlightDTO.fromVol(flights[0])));
        assertThrows(ResourceNotFoundException.class, () -> controller.deleteVol(dumbVol));
    }

}
*/
