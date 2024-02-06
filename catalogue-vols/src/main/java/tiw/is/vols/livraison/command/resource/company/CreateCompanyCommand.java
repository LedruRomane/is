package tiw.is.vols.livraison.command.resource.company;

import tiw.is.server.commandBus.ICommand;

/**
 * Command record for company creation.
 *
 * @param id ID of the new company that we want to create.
 */
public record CreateCompanyCommand(String id) implements ICommand {}
