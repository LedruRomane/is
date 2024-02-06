package tiw.is.vols.livraison.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.company.GetCompanyCommand;
import tiw.is.server.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Optional;

public class GetCompanyCommandHandler implements ICommandHandler<Company, GetCompanyCommand> {
    private final CompanyDao dao;

    public GetCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    /**
     * Execution : get One company from his ID.
     *
     * @param command payload that contain Company's ID.
     * @return Company.
     */
    public Company handle(GetCompanyCommand command) throws ResourceNotFoundException {
        return Optional.ofNullable(dao.getOneById(command.id())).orElseThrow(
                () -> new ResourceNotFoundException("La compagnie " + command.id() + " n'existe pas.")
        );
    }
}
