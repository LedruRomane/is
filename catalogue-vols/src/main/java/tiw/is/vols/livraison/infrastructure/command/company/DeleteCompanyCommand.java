package tiw.is.vols.livraison.infrastructure.command.company;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Record that provide getter or setter implicitly.
 * @param id String id of the company that we want to delete (payload).
 */
public record DeleteCompanyCommand(String id) implements ICommand {}
