package tiw.is.vols.livraison.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.company.GetCompaniesCommand;
import tiw.is.server.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Collection;

public class GetCompaniesCommandHandler implements ICommandHandler<Collection<Company>, GetCompaniesCommand> {
    private final CompanyDao dao;

    public GetCompaniesCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    /**
     * Execution for FindAll companies.
     *
     * @param command
     * @return Collection of company.
     * @throws ResourceNotFoundException
     */
    public Collection<Company> handle(GetCompaniesCommand command) throws ResourceNotFoundException {
        return dao.getAll();
    }
}
