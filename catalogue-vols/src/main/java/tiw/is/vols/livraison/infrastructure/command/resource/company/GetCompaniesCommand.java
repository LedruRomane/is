package tiw.is.vols.livraison.infrastructure.command.resource.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for getting all companies.
 */
public record GetCompaniesCommand() implements ICommand {
}
