package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.DataAccessObjectTest;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.CreateOrUpdateFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.DeleteFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.GetFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.GetFlightsCommandHandler;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FlightOperationTest extends DataAccessObjectTest {
    private GetFlightCommandHandler getCommandHandler;
    private GetFlightsCommandHandler getAllCommandHandler;
    private CreateOrUpdateFlightCommandHandler createOrUpdateCommandHandler;
    private DeleteFlightCommandHandler deleteCommandHandler;
    private final FlightDTO dumbVol = new FlightDTO("UFO Mogul", "USAF", "Roswell");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        this.getCommandHandler = new GetFlightCommandHandler(flightDao);
        this.getAllCommandHandler = new GetFlightsCommandHandler(flightDao);
        this.createOrUpdateCommandHandler = new CreateOrUpdateFlightCommandHandler(flightDao, companyDao);
        this.deleteCommandHandler = new DeleteFlightCommandHandler(flightDao);
    }

    @Test
    void getVols() throws ResourceNotFoundException {
        Collection<FlightDTO> vols1 = getAllCommandHandler.handle(new GetFlightsCommand());
        for (FlightDTO v : vols1) {
            assertTrue(Arrays.stream(flights).toList().contains(new Flight(
                    v.id(),
                    companyDao.getOneById(v.company()), v.pointLivraisonBagages())));
        }
        for (Flight v : flights) {
            assertTrue(vols1.contains(FlightDTO.fromFlight(v)));
        }
    }


    @Test
    void getVol() {
        try {
            assertEquals(FlightDTO.fromFlight(flights[0]),
                    getCommandHandler.handle(new GetFlightCommand(
                            FlightDTO.fromFlight(flights[0]).id()
                    )));
        } catch (ResourceNotFoundException e) {
            fail();
        }
        assertThrows(ResourceNotFoundException.class, () ->
                getCommandHandler.handle(new GetFlightCommand(dumbVol.id())));
    }


    @Test
    void createVol() {
        companyDao.save(new Company(dumbVol.company()));
        try {
            createOrUpdateCommandHandler.handle(new CreateOrUpdateFlightCommand(
                    dumbVol.id(),
                    dumbVol.company(),
                    dumbVol.pointLivraisonBagages()
            ));
            assertNotNull(companyDao.getOneById(dumbVol.company()));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateVol() {
        String nouveauPointLivraison = "Pétaouchnok";
        FlightDTO dto = new FlightDTO(flights[0].getId(), companies[0].getId(), nouveauPointLivraison);
        try {
            createOrUpdateCommandHandler.handle(new CreateOrUpdateFlightCommand(
                    dto.id(),
                    dto.company(),
                    dto.pointLivraisonBagages()
            ));
            assertEquals(flightDao.getOneById(flights[0].getId()).getPointLivraisonBagages(), nouveauPointLivraison);
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteVol() {
        assertDoesNotThrow(() -> deleteCommandHandler.handle(new DeleteFlightCommand(
                FlightDTO.fromFlight(flights[0]).id()
        )));
        assertThrows(ResourceNotFoundException.class, () -> deleteCommandHandler.handle(new DeleteFlightCommand(
                dumbVol.id()
        )));
    }

}
