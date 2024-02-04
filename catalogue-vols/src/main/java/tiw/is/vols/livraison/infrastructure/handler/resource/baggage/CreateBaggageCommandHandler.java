package tiw.is.vols.livraison.infrastructure.handler.resource.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.CreateBaggageCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler that provide the Creation of a company.
 * We inject the controller that provide the operations for the creation.
 * Should implement the HandlerInterface to ensure a strong typing check.
 */
public class CreateBaggageCommandHandler implements ICommandHandler<BaggageDTO, CreateBaggageCommand> {
    static final Logger logger = LoggerFactory.getLogger(CreateBaggageCommandHandler.class);

    private final BaggageDao dao;
    private final FlightDao flightDao;
    public CreateBaggageCommandHandler(BaggageDao dao, FlightDao flightDao) {
        this.dao = dao;
        this.flightDao = flightDao;
    }

    /**
     * Execution.
     * @param command Command injected, that provide the payload (body request).
     * @return company created.
     * @throws ResourceAlreadyExistsException
     * @throws ResourceNotFoundException
     * @throws ClassCastException
     */
    public BaggageDTO handle(CreateBaggageCommand command) throws ResourceAlreadyExistsException, ResourceNotFoundException, ClassCastException {
        Flight flight = flightDao.getOneById(command.id());
        if(flight == null) {
            throw new ResourceNotFoundException("The flight doesn't exist: " + command.id());
        }

        try {
            float weight = Float.parseFloat(command.weight());
            Baggage baggage = flight.createBagage(weight, command.passenger());

            if (dao.getOneById(flight.getId(), baggage.getNumero()) != null)
                throw new ResourceAlreadyExistsException("This baggage already exist" + baggage.getNumero());
            dao.save(baggage);

            return new BaggageDTO(baggage.getFlight().getId(), baggage.getNumero(), baggage.getWeight(), baggage.getPassenger());

        } catch (ClassCastException e) {
            throw new ClassCastException("Weight isn't a float type: " + command.weight());
        }
    }
}
