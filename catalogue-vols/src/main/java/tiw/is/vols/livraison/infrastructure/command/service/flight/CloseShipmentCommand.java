package tiw.is.vols.livraison.infrastructure.command.service.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record : close a shipment.
 *
 * @param id Flight's ID.
 */
public record CloseShipmentCommand(String id) implements ICommand {
}

