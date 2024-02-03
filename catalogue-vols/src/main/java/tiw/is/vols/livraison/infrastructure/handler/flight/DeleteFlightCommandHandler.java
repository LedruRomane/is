package tiw.is.vols.livraison.infrastructure.handler.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;

public class DeleteFlightCommandHandler implements ICommandHandler<Boolean, DeleteFlightCommand> {
    private final FlightDao dao;

    public DeleteFlightCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    public Boolean handle(DeleteFlightCommand command) throws ResourceNotFoundException {
        if(!dao.deleteOneById(command.id()))
            throw new ResourceNotFoundException("Le vol " + command.id() + " n'existe pas.");
        return true;
    }
}
