package tiw.is.vols.livraison.handler.resource.flight;

import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.flight.DeleteFlightCommand;
import tiw.is.server.commandBus.ICommandHandler;

public class DeleteFlightCommandHandler implements ICommandHandler<Boolean, DeleteFlightCommand> {
    private final FlightDao dao;

    public DeleteFlightCommandHandler(FlightDao dao) {
        this.dao = dao;
    }

    /**
     * Execution: delete a Flight.
     *
     * @param command Flight's ID.
     * @return true if deleted.
     * @throws ResourceNotFoundException
     */
    public Boolean handle(DeleteFlightCommand command) throws ResourceNotFoundException {
        if(!dao.deleteOneById(command.id()))
            throw new ResourceNotFoundException("Le vol " + command.id() + " n'existe pas.");
        return true;
    }
}
