package tiw.is.vols.livraison.infrastructure.command.resource.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for delete a Flight.
 *
 * @param id String id of the Flight that we want to delete.
 */
public record DeleteFlightCommand(String id) implements ICommand {
}
