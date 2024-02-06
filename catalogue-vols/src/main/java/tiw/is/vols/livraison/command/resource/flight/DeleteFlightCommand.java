package tiw.is.vols.livraison.command.resource.flight;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for delete a Flight.
 *
 * @param id String id of the Flight that we want to delete.
 */
public record DeleteFlightCommand(String id) implements ICommand {
}
