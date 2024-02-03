package tiw.is.vols.livraison.infrastructure.command.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Record that provide getter or setter implicitly.
 * @param id String id of the new flight that we want to create or update if founded (payload).
 */
public record CreateOrUpdateFlightCommand(String id, String companyID, String pointLivraisonBagages  ) implements ICommand {}
