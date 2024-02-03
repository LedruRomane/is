package tiw.is.vols.livraison.infrastructure.command.resource.baggage;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Record that provide getter or setter implicitly.
 * @param id String id of the flight
 * @param num num of the baggage that we want to delete (payload).
 */
public record DeleteBaggageCommand(String id, int num) implements ICommand {}
