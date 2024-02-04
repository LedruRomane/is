package tiw.is.vols.livraison.infrastructure.command.service.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

public record CloseShipmentCommand (String id) implements ICommand {}

