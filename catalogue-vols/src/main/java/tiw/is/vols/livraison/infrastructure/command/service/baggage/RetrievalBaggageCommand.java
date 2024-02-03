package tiw.is.vols.livraison.infrastructure.command.service.baggage;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

public record RetrievalBaggageCommand(String id, int num) implements ICommand {}
