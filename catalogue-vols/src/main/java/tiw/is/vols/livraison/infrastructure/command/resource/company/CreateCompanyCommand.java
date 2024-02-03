package tiw.is.vols.livraison.infrastructure.command.resource.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Record that provide getter or setter implicitly.
 * @param id String id of the new company that we want to create (payload).
 */
public record CreateCompanyCommand(String id) implements ICommand {}
