package tiw.is.vols.livraison.infrastructure.command.service.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

public record GetUnclaimedBaggagesCommand(String id) implements ICommand {}