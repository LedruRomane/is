package tiw.is.vols.livraison.command.resource.flight;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record get a flight with his ID.
 *
 * @param id Flight's ID.
 */
public record GetFlightCommand(String id) implements ICommand {
}