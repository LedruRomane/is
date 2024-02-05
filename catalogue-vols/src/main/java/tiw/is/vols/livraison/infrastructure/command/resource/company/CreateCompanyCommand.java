package tiw.is.vols.livraison.infrastructure.command.resource.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for company creation.
 *
 * @param id ID of the new company that we want to create.
 */
public record CreateCompanyCommand(String id) implements ICommand {}
