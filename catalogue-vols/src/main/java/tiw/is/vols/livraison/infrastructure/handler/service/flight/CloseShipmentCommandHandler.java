package tiw.is.vols.livraison.infrastructure.handler.service.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.service.flight.CloseShipmentCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Flight;

import java.util.Optional;

public class CloseShipmentCommandHandler implements ICommandHandler<Boolean, CloseShipmentCommand> {

    private final FlightDao dao;

    public CloseShipmentCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

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