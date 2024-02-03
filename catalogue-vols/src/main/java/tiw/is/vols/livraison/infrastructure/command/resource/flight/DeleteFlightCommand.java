package tiw.is.vols.livraison.infrastructure.command.resource.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Record that provide getter or setter implicitly.
 * @param id String id of the Flight that we want to delete (payload).
 */
public record DeleteFlightCommand(String id) implements ICommand {}
