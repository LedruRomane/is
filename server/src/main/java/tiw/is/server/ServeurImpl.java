package tiw.is.server;

import jakarta.persistence.EntityManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.infrastructure.command.company.CreateCompanyCommand;
import tiw.is.vols.livraison.exception.CommandNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.*;
import tiw.is.vols.livraison.infrastructure.handler.company.CreateCompanyCommandHandler;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.infrastructure.handler.company.DeleteCompanyCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.company.GetCompaniesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.company.GetCompanyCommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;

    private static JsonFormatter<Company> companyFormatter;

    private final static Logger LOG = LoggerFactory.getLogger(ServeurImpl.class);


    /**
     * Constructor Server, implement container and provide services & components.
     */
    public ServeurImpl() {
        companyFormatter = new JsonFormatter<>();

        // todo: provide a configuration file instead.

        // is picoContainer have a lazy mode ?
        this.picoContainer = new PicoBuilder(new ConstructorInjection()).withCaching().build();

        // Register the entity manager:
        EntityManager em = PersistenceManager.createEntityManagerFactory().createEntityManager();
        picoContainer.addComponent(em);

        // Register the DAO and Controller :
        picoContainer.addComponent(CompanyDao.class);

        // Register the message bus handlers:
        picoContainer.addComponent(CreateCompanyCommandHandler.class);
        picoContainer.addComponent(GetCompanyCommandHandler.class);
        picoContainer.addComponent(GetCompaniesCommandHandler.class);
        picoContainer.addComponent(DeleteCompanyCommandHandler.class);

        // Create the handler service locator and register it.
        // maybe we need a disambiguation using the Parameter Object ?
        Map<Class, ICommandHandler> handlerLocator = new HashMap<>();
        // todo: modify this into the conf file. All handlers with their command should be passed into handlerLocator services
        // pattern Annuaire : pattern service locator
        handlerLocator.put(CreateCompanyCommand.class, picoContainer.getComponent(CreateCompanyCommandHandler.class));
        handlerLocator.put(GetCompanyCommand.class, picoContainer.getComponent(GetCompanyCommandHandler.class));
        handlerLocator.put(GetCompaniesCommand.class, picoContainer.getComponent(GetCompaniesCommandHandler.class));
        handlerLocator.put(DeleteCompanyCommand.class, picoContainer.getComponent(DeleteCompanyCommandHandler.class));

        picoContainer.addComponent(handlerLocator);

        // Register the message bus middlewares:
        picoContainer.addComponent(TransactionMiddleware.class);
        picoContainer.addComponent(HandlerMiddleware.class);

        // Create middleware queue we need for commandbus :
        Collection<IMiddleware> middleware = new ArrayList<>();
        middleware.add(picoContainer.getComponent(TransactionMiddleware.class));
        middleware.add(picoContainer.getComponent(HandlerMiddleware.class));

        // Create the command bus service and register it.
        CommandBus cm = new CommandBus(middleware);
        picoContainer.addComponent(cm);

    }

    private CommandBus getCommandBus() {
        return picoContainer.getComponent(CommandBus.class);
    }

    public EntityManager getEntityManager() {
        return picoContainer.getComponent(EntityManager.class);
    }

    /**
     * Unique endpoint simulate API queries or mutations.
     * @param command String simulate a path like '/companies' -> 'getCompanies' (keyword)
     * @param params Object (json) simulate the body sent
     * @return serialized Object
     */
    public Object processRequest(String command, Map<String, Object> params) {
        try {
            return switch (command) {
                case "createCompany" -> companyFormatter.serializeObject(
                        // todo: cleaner ? -> check the type passed to the command bus handle method.
                        this.getCommandBus().handle(new CreateCompanyCommand((String) params.get("id")))
                );
                case "getCompany" -> companyFormatter.serializeObject(
                        this.getCommandBus().handle(new GetCompanyCommand((String) params.get("id")))
                );
                case "getCompanies" -> companyFormatter.serializeObject(
                        this.getCommandBus().handle(new GetCompaniesCommand())
                );
                case "deleteCompany" -> companyFormatter.serializeObject(
                        this.getCommandBus().handle(new DeleteCompanyCommand((String) params.get("id")))
                );
                // todo: etc...
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return ?
        }
    }
}

