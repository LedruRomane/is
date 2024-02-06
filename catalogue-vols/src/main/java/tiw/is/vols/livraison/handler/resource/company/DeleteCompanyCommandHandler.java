package tiw.is.vols.livraison.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.company.DeleteCompanyCommand;
import tiw.is.server.commandBus.ICommandHandler;

public class DeleteCompanyCommandHandler implements ICommandHandler<Boolean, DeleteCompanyCommand> {
    private final CompanyDao dao;

    public DeleteCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    /**
     * Execution delete a company.
     *
     * @param command Command injected, that provide the payload (body request).
     * @return boolean, true if company deleted.
     * @throws ResourceNotFoundException
     */
    public Boolean handle(DeleteCompanyCommand command) throws ResourceNotFoundException {
        if (!dao.deleteOneById(command.id()))
            throw new ResourceNotFoundException("La compagnie " + command.id() + " n'existe pas.");
        return true;
    }
}
