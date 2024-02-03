package tiw.is.server;

import jakarta.persistence.EntityManager;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.livraison.dao.BaggageDao;
import tiw.is.vols.livraison.dao.CompanyDao;
import tiw.is.vols.livraison.dao.FlightDao;
import tiw.is.vols.livraison.infrastructure.command.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.company.CreateCompanyCommand;
import tiw.is.vols.livraison.exception.CommandNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.command.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.*;
import tiw.is.vols.livraison.infrastructure.handler.baggage.GetBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.baggage.GetBaggagesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.company.CreateCompanyCommandHandler;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.dao.CatalogCompany;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.infrastructure.handler.company.DeleteCompanyCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.company.GetCompaniesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.company.GetCompanyCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.flight.CreateOrUpdateFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.flight.DeleteFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.flight.GetFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.flight.GetFlightsCommandHandler;
import tiw.is.vols.livraison.model.Company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;

    private static JsonFormatter<Company> formatter;

    private final static Logger LOG = LoggerFactory.getLogger(ServeurImpl.class);


    /**
     * Constructor Server, implement container and provide services & components.
     */
    public ServeurImpl() {
        formatter = new JsonFormatter<>();

        // todo: provide a configuration file instead.

        // is picoContainer have a lazy mode ?
        this.picoContainer = new PicoBuilder(new ConstructorInjection()).withCaching().build();

        // Register the entity manager:
        EntityManager em = PersistenceManager.createEntityManagerFactory().createEntityManager();
        picoContainer.addComponent(em);

        // Register the DAO and Controller :
        picoContainer.addComponent(CompanyDao.class);
        picoContainer.addComponent(FlightDao.class);
        picoContainer.addComponent(BaggageDao.class);

        // Register the message bus handlers: //todo: conf file
        picoContainer.addComponent(CreateCompanyCommandHandler.class);
        picoContainer.addComponent(GetCompanyCommandHandler.class);
        picoContainer.addComponent(GetCompaniesCommandHandler.class);
        picoContainer.addComponent(DeleteCompanyCommandHandler.class);
        picoContainer.addComponent(CreateOrUpdateFlightCommandHandler.class);
        picoContainer.addComponent(GetFlightsCommandHandler.class);
        picoContainer.addComponent(GetFlightCommandHandler.class);
        picoContainer.addComponent(DeleteFlightCommandHandler.class);
        picoContainer.addComponent(GetBaggagesCommandHandler.class);
        picoContainer.addComponent(GetBaggageCommandHandler.class);

        // Create the handler service locator and register it.
        // maybe we need a disambiguation using the Parameter Object ?
        Map<Class, ICommandHandler> handlerLocator = new HashMap<>();
        // todo: modify this into the conf file. All handlers with their command should be passed into handlerLocator services
        // pattern Annuaire : pattern service locator ;)
        handlerLocator.put(CreateCompanyCommand.class, picoContainer.getComponent(CreateCompanyCommandHandler.class));
        handlerLocator.put(GetCompanyCommand.class, picoContainer.getComponent(GetCompanyCommandHandler.class));
        handlerLocator.put(GetCompaniesCommand.class, picoContainer.getComponent(GetCompaniesCommandHandler.class));
        handlerLocator.put(DeleteCompanyCommand.class, picoContainer.getComponent(DeleteCompanyCommandHandler.class));
        handlerLocator.put(CreateOrUpdateFlightCommand.class, picoContainer.getComponent(CreateOrUpdateFlightCommandHandler.class));
        handlerLocator.put(GetFlightsCommand.class, picoContainer.getComponent(GetFlightsCommandHandler.class));
        handlerLocator.put(GetFlightCommand.class, picoContainer.getComponent(GetFlightCommandHandler.class));
        handlerLocator.put(DeleteFlightCommand.class, picoContainer.getComponent(DeleteFlightCommandHandler.class));
        handlerLocator.put(GetBaggagesCommand.class, picoContainer.getComponent(GetBaggagesCommandHandler.class));
        handlerLocator.put(GetBaggageCommand.class, picoContainer.getComponent(GetBaggageCommandHandler.class));


        picoContainer.addComponent(handlerLocator);

        // Register the message bus middlewares:
        picoContainer.addComponent(TransactionMiddleware.class);
        picoContainer.addComponent(HandlerMiddleware.class);

        // Create middleware queue we need for commandbus :
        Collection<IMiddleware> middleware = new ArrayList<>();
        middleware.add(picoContainer.getComponent(TransactionMiddleware.class));
        middleware.add(picoContainer.getComponent(HandlerMiddleware.class));

        // Create the command bus service and register it.-
        picoContainer.addComponent(new CommandBus(middleware));

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
                //todo: Unchecked call to 'handle(C)' as a member of raw type 'tiw.is.vols.livraison.infrastructure.commandBus.CommandBus'
                case "createCompany" -> formatter.serializeObject(
                        this.getCommandBus().handle(new CreateCompanyCommand((String) params.get("id")))
                );
                case "getCompany" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetCompanyCommand((String) params.get("id")))
                );
                case "getCompanies" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetCompaniesCommand())
                );
                case "deleteCompany" -> formatter.serializeObject(
                        this.getCommandBus().handle(new DeleteCompanyCommand((String) params.get("id")))
                );
                case "createFlight", "updateFlight" -> formatter.serializeObject(
                        this.getCommandBus().handle(new CreateOrUpdateFlightCommand(
                                (String) params.get("id"),
                                (String) params.get("companyId"),
                                (String) params.get("pointLivraisonBagages")
                        ))
                );
                case "getFlights" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetFlightsCommand())
                );
                case "getFlight" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetFlightCommand((String) params.get("id")))
                );
                case "deleteFlight" -> formatter.serializeObject(
                        this.getCommandBus().handle(new DeleteFlightCommand((String) params.get("id")))
                );
                case "getBaggages" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetBaggagesCommand())
                );
                case "getBaggage" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetBaggageCommand(
                                (String) params.get("id"),
                                (int) params.get("num")
                        ))
                );
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return ?
        }
    }
}

