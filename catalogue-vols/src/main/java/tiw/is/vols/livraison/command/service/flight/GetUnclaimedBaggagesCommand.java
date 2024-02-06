package tiw.is.vols.livraison.command.service.flight;

import tiw.is.server.commandBus.ICommand;

/**
 * Command to get all flight's unclaimed baggages.
 * Losts baggages don't appear like an unclaimed baggage.
 *
 * @param id Flight's ID.
 */
public record GetUnclaimedBaggagesCommand(String id) implements ICommand {
}