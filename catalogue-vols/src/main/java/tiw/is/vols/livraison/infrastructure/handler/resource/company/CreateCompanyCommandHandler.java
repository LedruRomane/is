package tiw.is.vols.livraison.infrastructure.handler.resource.company;

import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.infrastructure.command.resource.company.CreateCompanyCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.ICommandHandler;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.model.Company;

/**
 * Handler that provide the Creation of a company.
 * We inject the controller that provide the operations for the creation.
 * Should implement the HandlerInterface to ensure a strong typing check.
 */
public class CreateCompanyCommandHandler implements ICommandHandler<Company, CreateCompanyCommand> {

    private final CompanyDao dao;
    public CreateCompanyCommandHandler(CompanyDao dao) {
        this.dao = dao;
    }

    /**
     * Execution.
     * @param command Command injected, that provide the payload (body request).
     * @return company created.
     * @throws ResourceAlreadyExistsException
     */
    public Company handle(CreateCompanyCommand command) throws ResourceAlreadyExistsException {
        Company company = new Company(command.id());
        if (dao.getOneById(company.getId()) != null)
            throw new ResourceAlreadyExistsException("Une compagnie avec l'ID " + company.getId() + " existe déjà.");
        dao.save(company);

        return company;
    }
}
