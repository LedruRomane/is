package tiw.is.vols.livraison.infrastructure.handler.resource.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;

import java.util.Collection;

public class GetFlightsCommandHandler implements ICommandHandler<Collection<FlightDTO>, GetFlightsCommand> {
    private final FlightDao dao;

    public GetFlightsCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    public Collection<FlightDTO> handle(GetFlightsCommand command) throws ResourceNotFoundException {
        return dao.getAll().stream().map(FlightDTO::fromFlight).toList();
    }
}
