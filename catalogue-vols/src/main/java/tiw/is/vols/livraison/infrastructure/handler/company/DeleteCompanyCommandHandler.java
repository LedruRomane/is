package tiw.is.vols.livraison.infrastructure.handler.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.CreateCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.model.Company;

public class DeleteCompanyCommandHandler implements ICommandHandler<Boolean, DeleteCompanyCommand> {
    private final CompanyDao dao;

    public DeleteCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    public Boolean handle(DeleteCompanyCommand command) throws ResourceNotFoundException {
        Company company = new Company(command.id());
        if(!dao.deleteOneById(company.getId()))
            throw new ResourceNotFoundException("La compagnie " + company.getId() + " n'existe pas.");
        return true;
    }
}
