package tiw.is.vols.livraison.command.resource.baggage;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for get one baggage from its ID.
 *
 * @param id  Flight ID.
 * @param num Baggage Num.
 */
public record GetBaggageCommand(String id, int num) implements ICommand {
}