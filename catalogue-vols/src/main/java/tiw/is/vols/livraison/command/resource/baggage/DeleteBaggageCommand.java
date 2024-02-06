package tiw.is.vols.livraison.command.resource.baggage;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for baggage deletion.
 *
 * @param id  String id of the flight.
 * @param num Num of the baggage that we want to delete (payload).
 */
public record DeleteBaggageCommand(String id, int num) implements ICommand {
}
