package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.DataAccessObjectTest;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.CreateBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.DeleteBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.CreateBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.DeleteBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.GetBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.GetBaggagesCommandHandler;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Company;
import tiw.is.vols.livraison.model.Flight;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BagageOperationTest extends DataAccessObjectTest {

    private GetBaggageCommandHandler getCommandHandler;
    private GetBaggagesCommandHandler getAllCommandHandler;
    private CreateBaggageCommandHandler createCommandHandler;
    private DeleteBaggageCommandHandler deleteCommandHandler;

    private final BaggageDTO dumbBagage = new BaggageDTO(
            "Crazy flying saucer",
            0, // Inutilisé pour la création
            -10, // (c'est de l'antimatière)
            "E.T.");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        this.getCommandHandler = new GetBaggageCommandHandler(baggageDao);
        this.getAllCommandHandler = new GetBaggagesCommandHandler(baggageDao);
        this.createCommandHandler = new CreateBaggageCommandHandler(baggageDao, flightDao);
        this.deleteCommandHandler = new DeleteBaggageCommandHandler(baggageDao);
    }

    @Test
    void getBagages() throws ResourceNotFoundException {
        Collection<BaggageDTO> bagages1 = getAllCommandHandler.handle(new GetBaggagesCommand());
        for (BaggageDTO b : bagages1) {
            assertTrue(Arrays.stream(baggages).toList().contains(new Baggage(flightDao.getOneById(b.flightId()), b.numero(), b.weight(), b.passenger())));
        }
        for (Baggage b : baggages) {
            assertTrue(bagages1.contains(BaggageDTO.fromBaggage(b)));
        }
    }

    @Test
    void getBagage() {
        try {
            assertEquals(
                    BaggageDTO.fromBaggage(baggages[0]),
                    getCommandHandler.handle(new GetBaggageCommand(
                            BaggageDTO.fromBaggage(baggages[0]).flightId(),
                            BaggageDTO.fromBaggage(baggages[0]).numero()
                    )));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> getCommandHandler.handle(
                new GetBaggageCommand("Not exist", 2)
        ));
    }

    @Test
    void createBagage() throws ResourceAlreadyExistsException {
        try {
            flightDao.save(new Flight(dumbBagage.flightId(), companyDao.save(new Company("ALF Inc.")), "Jupiter"));
            BaggageDTO res = createCommandHandler.handle(new CreateBaggageCommand(
                    dumbBagage.flightId(),
                    String.valueOf(dumbBagage.weight()),
                    dumbBagage.passenger()
            ));
            assertNotNull(baggageDao.getOneById(dumbBagage.flightId(), res.numero()));
            assertEquals(dumbBagage.flightId(), res.flightId());
            assertEquals(dumbBagage.weight(), res.weight());
            assertEquals(dumbBagage.passenger(), res.passenger());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteBagage() {
        assertDoesNotThrow(() -> deleteCommandHandler.handle(new DeleteBaggageCommand(BaggageDTO.fromBaggage(baggages[0]).flightId(), BaggageDTO.fromBaggage(baggages[0]).numero())));
        assertThrows(ResourceNotFoundException.class, () -> deleteCommandHandler.handle(new DeleteBaggageCommand("not exist", 1)));
    }
}
