package tiw.is.vols.livraison.command.service.baggage;


import tiw.is.server.commandBus.ICommand;

/**
 * Command that make a baggage tagged deliver.
 *
 * @param id  Flight's ID.
 * @param num Baggage's Num.
 */
public record DeliverBaggageCommand(String id, int num) implements ICommand {
}
