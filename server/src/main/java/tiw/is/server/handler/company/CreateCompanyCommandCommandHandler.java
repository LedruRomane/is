package tiw.is.server.handler.company;

import tiw.is.server.command.company.CreateCompanyCommand;
import tiw.is.server.commandBus.ICommandHandler;
import tiw.is.vols.livraison.controller.resource.CompanyOperationController;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.model.Company;

/**
 * Handler that provide the Creation of a company.
 * We inject the controller that provide the operations for the creation.
 * Should implement the HandlerInterface to ensure a strong typing check.
 */
public class CreateCompanyCommandCommandHandler implements ICommandHandler<Company, CreateCompanyCommand> {

    private final CompanyOperationController companyController;
    public CreateCompanyCommandCommandHandler(CompanyOperationController controller) {
        companyController = controller;
    }

    /**
     * Execution.
     * @param command Command injected, that provide the payload (body request).
     * @return company created.
     * @throws ResourceAlreadyExistsException
     */
    public Company handle(CreateCompanyCommand command) throws ResourceAlreadyExistsException {
        Company company = new Company(command.id());
        companyController.createCompany(company);

        return company;
    }
}
