package tiw.is.vols.livraison.infrastructure.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Optional;

public class GetCompanyCommandHandler implements ICommandHandler<Company, GetCompanyCommand> {

    private final CompanyDao dao;
    public GetCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    /**
     * Renvoie une compagnie en fonction de son id.
     *
     * @param command payload qui contient l'id de la compagnie cherchée
     * @return la compagnie trouvée ou null si aucune compagnie n'a été trouvée
     */
    public Company handle(GetCompanyCommand command) throws ResourceNotFoundException {
        return Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + command.id() + " n'existe pas.")
        );
    }
}
