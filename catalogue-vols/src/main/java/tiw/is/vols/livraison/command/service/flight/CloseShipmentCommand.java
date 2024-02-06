package tiw.is.vols.livraison.command.service.flight;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record : close a shipment.
 *
 * @param id Flight's ID.
 */
public record CloseShipmentCommand(String id) implements ICommand {
}

