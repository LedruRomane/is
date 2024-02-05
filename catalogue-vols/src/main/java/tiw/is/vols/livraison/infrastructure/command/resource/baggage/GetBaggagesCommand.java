package tiw.is.vols.livraison.infrastructure.command.resource.baggage;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for getting all baggages.
 */
public record GetBaggagesCommand() implements ICommand {
}
