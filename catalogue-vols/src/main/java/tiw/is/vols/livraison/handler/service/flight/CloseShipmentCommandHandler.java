package tiw.is.vols.livraison.handler.service.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.service.flight.CloseShipmentCommand;
import tiw.is.server.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class CloseShipmentCommandHandler implements ICommandHandler<Boolean, CloseShipmentCommand> {

    private final FlightDao dao;

    public CloseShipmentCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    /**
     * Execution: close a flight baggage delivery.
     *
     * @param command Flight's ID.
     * @return true if flight's closed.
     * @throws ResourceNotFoundException
     */
    public Boolean handle(CloseShipmentCommand command) throws ResourceNotFoundException {
        Flight flight = Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("Le flight " + command.id() + " n'existe pas.")
        );
        if (!flight.isLivraisonEnCours())
            throw new IllegalStateException("Impossible de fermer une livraison déjà fermée.");

        flight.fermerLivraison();
        dao.save(flight);

        return true;
    }
}
