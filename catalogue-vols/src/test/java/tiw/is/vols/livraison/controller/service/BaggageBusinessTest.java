package tiw.is.vols.livraison.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.DataAccessObjectTest;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.DeliverBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.RetrievalBaggageCommand;
import tiw.is.vols.livraison.infrastructure.handler.service.baggage.DeliverBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.baggage.RetrievalBaggageCommandHandler;

import static org.junit.jupiter.api.Assertions.*;

class BaggageBusinessTest extends DataAccessObjectTest {
    private DeliverBaggageCommandHandler deliverCommandHandler;
    private RetrievalBaggageCommandHandler retrievalCommandHandler;
    private final BaggageDTO dumbBagage = new BaggageDTO(
            "Crazy flying saucer",
            0, //Inutilisé pour la création
            -10, // (c'est de l'antimatière)
            "E.T.");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        this.deliverCommandHandler = new DeliverBaggageCommandHandler(baggageDao, flightDao);
        this.retrievalCommandHandler = new RetrievalBaggageCommandHandler(baggageDao, flightDao);
    }

    @Test
    void delivrer() {
        try {
            deliverCommandHandler.handle(new DeliverBaggageCommand(
                    BaggageDTO.fromBaggage(baggages[0]).flightId(),
                    BaggageDTO.fromBaggage(baggages[0]).numero()
            ));
            assertTrue(baggages[0].isDelivre());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () ->
                deliverCommandHandler.handle(new DeliverBaggageCommand(
                        dumbBagage.flightId(),
                        dumbBagage.numero()
                )));
    }

    @Test
    void recuperer() {
        try {
            baggages[0].delivrer();
            retrievalCommandHandler.handle(new RetrievalBaggageCommand(
                    BaggageDTO.fromBaggage(baggages[0]).flightId(),
                    BaggageDTO.fromBaggage(baggages[0]).numero()
            ));
            assertTrue(baggages[0].isRecupere());
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(IllegalStateException.class, () ->
                retrievalCommandHandler.handle(new RetrievalBaggageCommand(
                        BaggageDTO.fromBaggage(baggages[1]).flightId(),
                        BaggageDTO.fromBaggage(baggages[1]).numero()
                )));
        assertThrows(ResourceNotFoundException.class, () ->
                retrievalCommandHandler.handle(new RetrievalBaggageCommand(
                        dumbBagage.flightId(),
                        dumbBagage.numero()
                )));
    }
}
