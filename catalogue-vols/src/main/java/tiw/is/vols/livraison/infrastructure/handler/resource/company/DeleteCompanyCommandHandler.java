package tiw.is.vols.livraison.infrastructure.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;

public class DeleteCompanyCommandHandler implements ICommandHandler<Boolean, DeleteCompanyCommand> {
    private final CompanyDao dao;

    public DeleteCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    public Boolean handle(DeleteCompanyCommand command) throws ResourceNotFoundException {
        if(!dao.deleteOneById(command.id()))
            throw new ResourceNotFoundException("La compagnie " + command.id() + " n'existe pas.");
        return true;
    }
}
