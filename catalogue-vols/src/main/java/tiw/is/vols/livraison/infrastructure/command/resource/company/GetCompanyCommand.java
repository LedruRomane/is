package tiw.is.vols.livraison.infrastructure.command.resource.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for get one company by his ID.
 *
 * @param id company ID.
 */
public record GetCompanyCommand(String id) implements ICommand {
}