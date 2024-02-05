package tiw.is.vols.livraison.infrastructure.command.service.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command to get flight's all lost baggages.
 *
 * @param id Flight's ID.
 */
public record GetLostBaggagesCommand(String id) implements ICommand {
}