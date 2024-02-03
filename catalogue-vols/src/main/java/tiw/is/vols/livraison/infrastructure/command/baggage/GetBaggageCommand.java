package tiw.is.vols.livraison.infrastructure.command.baggage;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

public record GetBaggageCommand(String id, int num) implements ICommand {}