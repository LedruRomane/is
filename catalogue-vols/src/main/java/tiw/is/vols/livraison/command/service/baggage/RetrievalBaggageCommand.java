package tiw.is.vols.livraison.command.service.baggage;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record that tag baggage as retrieve by his owner.
 *
 * @param id  Flight's ID.
 * @param num Baggage's ID.
 */
public record RetrievalBaggageCommand(String id, int num) implements ICommand {
}
