package tiw.is.vols.livraison.infrastructure.handler.service.flight;

import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.BaggageDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetLostBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Flight;

import java.util.Collection;
import java.util.Optional;

public class GetLostBaggagesCommandHandler implements ICommandHandler<Collection<BaggageDTO>, GetLostBaggagesCommand> {

    private final FlightDao dao;
    private final BaggageDao baggageDao;

    public GetLostBaggagesCommandHandler(FlightDao dao, BaggageDao baggageDao) {
        this.dao = dao;
        this.baggageDao = baggageDao;
    }

    public Collection<BaggageDTO> handle(GetLostBaggagesCommand command) throws ResourceNotFoundException, IllegalStateException {
        Flight flight = Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + command.id() + " n'existe pas.")
        );
        if (flight.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de lister les bagages perdus tant que la livraison est en cours.");

        return baggageDao.getBagagesPerdusByFlightId(flight.getId()).stream().map(BaggageDTO::fromBaggage).toList();
    }
}
