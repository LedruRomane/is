package tiw.is.vols.livraison.infrastructure.command.service.baggage;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command that make a baggage tagged deliver.
 *
 * @param id  Flight's ID.
 * @param num Baggage's Num.
 */
public record DeliverBaggageCommand(String id, int num) implements ICommand {
}
