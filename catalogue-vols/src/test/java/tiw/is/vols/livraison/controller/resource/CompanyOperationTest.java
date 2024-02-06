package tiw.is.vols.livraison.controller.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import tiw.is.vols.livraison.dao.DataAccessObjectTest;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.exception.ResourceNotFoundException;
import tiw.is.vols.livraison.command.resource.company.CreateCompanyCommand;
import tiw.is.vols.livraison.command.resource.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.command.resource.company.GetCompaniesCommand;
import tiw.is.vols.livraison.command.resource.company.GetCompanyCommand;
import tiw.is.vols.livraison.handler.resource.company.CreateCompanyCommandHandler;
import tiw.is.vols.livraison.handler.resource.company.DeleteCompanyCommandHandler;
import tiw.is.vols.livraison.handler.resource.company.GetCompaniesCommandHandler;
import tiw.is.vols.livraison.handler.resource.company.GetCompanyCommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CompanyOperationTest extends DataAccessObjectTest {

    private GetCompanyCommandHandler getCommandHandler;
    private GetCompaniesCommandHandler getAllCommandHandler;
    private CreateCompanyCommandHandler createCommandHandler;
    private DeleteCompanyCommandHandler deleteCommandHandler;
    private final Company dumbCompany = new Company("Mon beau navion");
    private final Company anotherCompany = new Company("nothing");

    @BeforeEach
    public void setup(TestInfo testInfo) {
        super.setup(testInfo);
        this.getCommandHandler = new GetCompanyCommandHandler(companyDao);
        this.getAllCommandHandler = new GetCompaniesCommandHandler(companyDao);
        this.deleteCommandHandler = new DeleteCompanyCommandHandler(companyDao);
        this.createCommandHandler = new CreateCompanyCommandHandler(companyDao);
    }

    @Test
    void getCompanies() throws ResourceNotFoundException {
        Collection<Company> comps = getAllCommandHandler.handle(new GetCompaniesCommand());
        for (Company c : comps) {
            assertTrue(Arrays.stream(companies).toList().contains(c));
        }
        for (Company c : companies) {
            assertTrue(comps.contains(c));
        }
    }

    @Test
    void getCompany() {
        try {
            assertEquals(companies[0], getCommandHandler.handle(
                    new GetCompanyCommand(companies[0].getId())));
        } catch (ResourceNotFoundException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceNotFoundException.class, () -> getCommandHandler.handle(
                new GetCompanyCommand(anotherCompany.getId())));
    }

    @Test
    void createCompany() {
        try {
            createCommandHandler.handle(new CreateCompanyCommand(dumbCompany.getId()));
            assertNotNull(companyDao.getOneById(dumbCompany.getId()));
        } catch (ResourceAlreadyExistsException e) {
            fail(e.getMessage());
        }
        assertThrows(ResourceAlreadyExistsException.class, () ->
                createCommandHandler.handle(new CreateCompanyCommand(
                        companies[0].getId()
                )));
    }

    @Test
    void deleteCompany() {
        assertDoesNotThrow(() -> deleteCommandHandler.handle(new DeleteCompanyCommand(
                companies[0].getId()
        )));
        assertThrows(ResourceNotFoundException.class, () -> deleteCommandHandler.handle(
                new DeleteCompanyCommand(anotherCompany.getId())
        ));
    }
}
