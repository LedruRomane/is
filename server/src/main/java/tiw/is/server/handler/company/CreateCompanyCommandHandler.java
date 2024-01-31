package tiw.is.server.handler.company;

import tiw.is.server.command.company.CreateCompanyCommand;
import tiw.is.server.commandBus.HandlerInterface;
import tiw.is.vols.livraison.controller.resource.CompanyOperationController;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.model.Company;

public class CreateCompanyCommandHandler implements HandlerInterface<Company, CreateCompanyCommand> {

    private final CompanyOperationController companyController;
    public CreateCompanyCommandHandler(CompanyOperationController controller) {
        companyController = controller;
    }

    public Company handle(CreateCompanyCommand command) throws ResourceAlreadyExistsException {
        Company company = new Company(command.id());
        companyController.createCompany(company);

        return company;
    }
}
