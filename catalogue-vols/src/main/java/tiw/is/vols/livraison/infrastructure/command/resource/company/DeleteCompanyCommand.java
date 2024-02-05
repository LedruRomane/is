package tiw.is.vols.livraison.infrastructure.command.resource.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for company deletion.
 *
 * @param id ID of the company that we want to delete.
 */
public record DeleteCompanyCommand(String id) implements ICommand {
}
