package tiw.is.vols.livraison.infrastructure.handler.service.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.DeliverBaggageCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class DeliverBaggageCommandHandler implements ICommandHandler<BaggageDTO, DeliverBaggageCommand> {
    private final BaggageDao dao;
    private final FlightDao flightDao;

    public DeliverBaggageCommandHandler(BaggageDao dao, FlightDao flightDao) {
        this.dao = dao;
        this.flightDao = flightDao;
    }

    public BaggageDTO handle(DeliverBaggageCommand command) throws ResourceNotFoundException {
        Flight flight = flightDao.getOneById(command.id());
        if(flight == null) {
            throw new ResourceNotFoundException("The flight doesn't exist: " + command.id());
        }

        Baggage baggage = Optional.ofNullable(dao.getOneById(flight.getId(), command.num())).orElseThrow(
                () -> new ResourceNotFoundException("Le baggage " + command.num() + " du vol " + flight.getId() + " n'existe pas."));

        baggage.delivrer();
        dao.update(baggage);

        return (new BaggageDTO(baggage.getFlight().getId(), baggage.getNumero(), baggage.getWeight(), baggage.getPassenger()));
    }
}
