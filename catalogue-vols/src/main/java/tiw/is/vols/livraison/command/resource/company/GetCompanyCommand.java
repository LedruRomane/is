package tiw.is.vols.livraison.command.resource.company;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for get one company by his ID.
 *
 * @param id company ID.
 */
public record GetCompanyCommand(String id) implements ICommand {
}