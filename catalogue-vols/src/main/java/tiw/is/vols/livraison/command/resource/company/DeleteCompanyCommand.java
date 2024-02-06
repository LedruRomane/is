package tiw.is.vols.livraison.command.resource.company;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for company deletion.
 *
 * @param id ID of the company that we want to delete.
 */
public record DeleteCompanyCommand(String id) implements ICommand {
}
