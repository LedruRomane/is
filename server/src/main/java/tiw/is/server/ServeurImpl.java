package tiw.is.server;

import jakarta.persistence.EntityManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.command.company.CreateCompanyCommand;
import tiw.is.server.commandBus.*;
import tiw.is.server.exception.CommandNotFoundException;
import tiw.is.server.handler.company.CreateCompanyCommandCommandHandler;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.controller.resource.CompanyOperationController;
import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.db.PersistenceManager;
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
        picoContainer.addComponent(CatalogCompany.class);
        picoContainer.addComponent(CompanyOperationController.class);

        // Register the message bus handlers:
        picoContainer.addComponent(CreateCompanyCommandCommandHandler.class);

        // Create the handler service locator and register it.
        Map<Class, ICommandHandler> handlerLocator = new HashMap<>();
        // todo: modify this into the conf file. All handlers with their command should be passed into handlerLocator services
        // pattern Annuaire
        handlerLocator.put(CreateCompanyCommand.class, picoContainer.getComponent(CreateCompanyCommandCommandHandler.class));

        picoContainer.addComponent(handlerLocator);

        // Register the message bus middlewares:
        picoContainer.addComponent(TransactionMiddleware.class);
        picoContainer.addComponent(HandlerMiddleware.class);

        Collection<IMiddleware> middleware = new ArrayList<>(); // looks like the conf file.
        middleware.add(picoContainer.getComponent(TransactionMiddleware.class));
        middleware.add(picoContainer.getComponent(HandlerMiddleware.class));

        // Create the command bus service and register it.
        CommandBus cm = new CommandBus(middleware);
        picoContainer.addComponent(cm);

    }

    private CommandBus getCommandBus() {
        return picoContainer.getComponent(CommandBus.class);
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
                        // cleaner ? -> check the type passed to the command bus handle method.
                        this.getCommandBus().handle(new CreateCompanyCommand((String) params.get("id")))
                );
                // case "deleteCompany" ->
                // case "getCompanies" ->
                // todo: etc...
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return ?
        }
    }
}

