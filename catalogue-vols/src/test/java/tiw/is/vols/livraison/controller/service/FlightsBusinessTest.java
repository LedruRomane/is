package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.DataAccessObjectTest;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.service.flight.CloseShipmentCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetLostBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetUnclaimedBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.CloseShipmentCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.GetLostBaggagesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.GetUnclaimedBaggagesCommandHandler;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FlightsBusinessTest extends DataAccessObjectTest {
    private CloseShipmentCommandHandler closeShipmentCommandHandler;
    private GetLostBaggagesCommandHandler getLostBaggagesCommandHandler;
    private GetUnclaimedBaggagesCommandHandler getUnclaimedBaggagesCommandHandler;
    private final FlightDTO dumbVol = new FlightDTO("UFO Mogul", "USAF", "Roswell");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        this.closeShipmentCommandHandler = new CloseShipmentCommandHandler(flightDao);
        this.getLostBaggagesCommandHandler = new GetLostBaggagesCommandHandler(flightDao, baggageDao);
        this.getUnclaimedBaggagesCommandHandler = new GetUnclaimedBaggagesCommandHandler(flightDao, baggageDao);
    }

    @Test
    void fermerLivraison() {
        assertDoesNotThrow(() ->
                closeShipmentCommandHandler.handle(new CloseShipmentCommand(
                        FlightDTO.fromFlight(flights[0]).id()
                )));
        assertThrows(ResourceNotFoundException.class, () ->
                closeShipmentCommandHandler.handle(new CloseShipmentCommand(
                        dumbVol.id()
                )));
    }

    @Test
    void bagagesPerdus() {
        flights[1].fermerLivraison();
        try {
            Collection<BaggageDTO> bagageDTOS =
                    getLostBaggagesCommandHandler.handle(new GetLostBaggagesCommand(
                            FlightDTO.fromFlight(flights[1]).id()
                    ));
            assertEquals(1,
                    bagageDTOS.size());
            var bDto = bagageDTOS.stream().findFirst().get();
            em.getTransaction().begin();
            baggageDao.getOneById(flights[1].getId(), bDto.numero()).delivrer();
            em.getTransaction().commit();
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        flights[1].fermerLivraison();
        try {
            assertEquals(0,
                    getLostBaggagesCommandHandler.handle(new GetLostBaggagesCommand(
                            FlightDTO.fromFlight(flights[1]).id()
                    )).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class,
                () -> getLostBaggagesCommandHandler.handle(new GetLostBaggagesCommand(
                        FlightDTO.fromFlight(flights[3]).id()
                )));
        assertThrows(ResourceNotFoundException.class, () ->
                getLostBaggagesCommandHandler.handle(new GetLostBaggagesCommand(
                        dumbVol.id()
                )));
    }


    @Test
    void bagagesNonRecuperes() {
        Flight vol = flights[1];
        vol.fermerLivraison();
        try {
            Collection<BaggageDTO> bagageDTOS = getUnclaimedBaggagesCommandHandler.handle(
                    new GetUnclaimedBaggagesCommand(FlightDTO.fromFlight(vol).id()));
            assertEquals(1, bagageDTOS.size());
            em.getTransaction().begin();
            for (var bDto : bagageDTOS) {
                var b = baggageDao.getOneById(bDto.flightId(), bDto.numero());
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
                    getUnclaimedBaggagesCommandHandler.handle(new GetUnclaimedBaggagesCommand(
                            FlightDTO.fromFlight(vol).id()
                    )).size());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () ->
                getUnclaimedBaggagesCommandHandler.handle(new GetUnclaimedBaggagesCommand(
                        FlightDTO.fromFlight(flights[2]).id()
                )));
        assertThrows(ResourceNotFoundException.class, () ->
                getUnclaimedBaggagesCommandHandler.handle(new GetUnclaimedBaggagesCommand(
                        dumbVol.id()
                )));
    }
}
