package tiw.is.vols.livraison.infrastructure.handler.resource.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.dto.FlightDTO;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class GetFlightCommandHandler implements ICommandHandler<FlightDTO, GetFlightCommand> {

    private final FlightDao dao;
    public GetFlightCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    /**
     * Renvoie un vol en fonction de son id.
     *
     * @param command payload qui contient l'id du vol cherché.
     * @return le vol trouvé ou null si aucun vol n'a été trouvé.
     */
    public FlightDTO handle(GetFlightCommand command) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + command.id() + " n'existe pas.")
        );

        return FlightDTO.fromFlight(flight);
    }
}
