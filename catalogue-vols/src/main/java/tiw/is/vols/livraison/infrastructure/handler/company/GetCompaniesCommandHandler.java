package tiw.is.vols.livraison.infrastructure.handler.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Collection;

public class GetCompaniesCommandHandler  implements ICommandHandler<Collection<Company>, GetCompaniesCommand> {
    private final CompanyDao dao;

    public GetCompaniesCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    public Collection<Company> handle(GetCompaniesCommand command) throws ResourceNotFoundException {
        return dao.getAll();
    }
}
