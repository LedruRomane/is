package tiw.is.vols.livraison.handler.service.baggage;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.service.baggage.RetrievalBaggageCommand;
import tiw.is.server.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Baggage;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class RetrievalBaggageCommandHandler implements ICommandHandler<BaggageDTO, RetrievalBaggageCommand> {
    private final BaggageDao dao;
    private final FlightDao flightDao;

    public RetrievalBaggageCommandHandler(BaggageDao dao, FlightDao flightDao) {
        this.dao = dao;
        this.flightDao = flightDao;
    }

    /**
     * Execution: mark baggage as retrieval by his owner.
     *
     * @param command Flight's ID and baggage's num.
     * @return BaggageDTO.
     * @throws ResourceNotFoundException
     * @throws IllegalStateException
     */
    public BaggageDTO handle(RetrievalBaggageCommand command) throws ResourceNotFoundException, IllegalStateException {
        Flight flight = flightDao.getOneById(command.id());
        if(flight == null) {
            throw new ResourceNotFoundException("The flight doesn't exist: " + command.id());
        }

        Baggage baggage = Optional.ofNullable(dao.getOneById(flight.getId(), command.num())).orElseThrow(
                () -> new ResourceNotFoundException("Le baggage " + command.num() + " du vol " + flight.getId() + " n'existe pas."));

        if (!baggage.isDelivre())
            throw new IllegalStateException("Un baggage ne peut être récupéré avant d'être délivré.");

        baggage.recuperer();
        dao.update(baggage);

        return (new BaggageDTO(baggage.getFlight().getId(), baggage.getNumero(), baggage.getWeight(), baggage.getPassenger()));
    }
}
