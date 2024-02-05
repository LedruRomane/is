package tiw.is.vols.livraison.infrastructure.command.resource.flight;

import tiw.is.vols.livraison.infrastructure.commandBus.ICommand;

/**
 * Command record for create or update a Flight.
 *
 * @param id String id of the new flight that we want to create or update.
 */
public record CreateOrUpdateFlightCommand(String id, String companyID, String pointLivraisonBagages) implements ICommand {}
