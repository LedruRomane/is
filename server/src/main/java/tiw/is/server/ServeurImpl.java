package tiw.is.server;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.persistence.EntityManager;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;
import org.picocontainer.monitors.NullComponentMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.livraison.db.PersistenceManager;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.CreateBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.DeleteBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.baggage.GetBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.CreateCompanyCommand;
import tiw.is.vols.livraison.exception.CommandNotFoundException;
import tiw.is.vols.livraison.infrastructure.command.resource.company.DeleteCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.GetCompaniesCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.company.GetCompanyCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.CreateOrUpdateFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.DeleteFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightCommand;
import tiw.is.vols.livraison.infrastructure.command.resource.flight.GetFlightsCommand;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.DeliverBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.service.baggage.RetrievalBaggageCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.CloseShipmentCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetLostBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.command.service.flight.GetUnclaimedBaggagesCommand;
import tiw.is.vols.livraison.infrastructure.commandBus.*;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.CreateBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.DeleteBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.GetBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.baggage.GetBaggagesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.company.CreateCompanyCommandHandler;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.infrastructure.handler.resource.company.DeleteCompanyCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.company.GetCompaniesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.company.GetCompanyCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.CreateOrUpdateFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.DeleteFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.GetFlightCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.resource.flight.GetFlightsCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.baggage.DeliverBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.baggage.RetrievalBaggageCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.CloseShipmentCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.GetLostBaggagesCommandHandler;
import tiw.is.vols.livraison.infrastructure.handler.service.flight.GetUnclaimedBaggagesCommandHandler;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;

    private static JsonFormatter formatter;

    private final static Logger LOG = LoggerFactory.getLogger(ServeurImpl.class);


    /**
     * Constructor Server, implement container and provide services & components.
     */
    public ServeurImpl() throws IOException {
        formatter = new JsonFormatter<>();
        String app = "application-config";
        JsonObject configJson;

        ComponentMonitor monitor = new NullComponentMonitor();
        this.picoContainer = new PicoBuilder(new ConstructorInjection())
                .withCaching()
                .withLifecycle(new StartableLifecycleStrategy(monitor))
                .build();

        String configContent = new String(Files.readAllBytes(Paths.get("src/main/resources/appConfiguration.json")));

        try (JsonReader reader = Json.createReader(new StringReader(configContent))) {
            configJson = reader.readObject();

            loadComponents(configJson.getJsonObject(app).getJsonArray("persistence-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("data-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("handlers-components"));

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
            handlerLocator.put(DeleteBaggageCommand.class, picoContainer.getComponent(DeleteBaggageCommandHandler.class));
            handlerLocator.put(CreateBaggageCommand.class, picoContainer.getComponent(CreateBaggageCommandHandler.class));
            handlerLocator.put(DeliverBaggageCommand.class, picoContainer.getComponent(DeliverBaggageCommandHandler.class));
            handlerLocator.put(RetrievalBaggageCommand.class, picoContainer.getComponent(RetrievalBaggageCommandHandler.class));
            handlerLocator.put(CloseShipmentCommand.class, picoContainer.getComponent(CloseShipmentCommandHandler.class));
            handlerLocator.put(GetLostBaggagesCommand.class, picoContainer.getComponent(GetLostBaggagesCommandHandler.class));
            handlerLocator.put(GetUnclaimedBaggagesCommand.class, picoContainer.getComponent(GetUnclaimedBaggagesCommandHandler.class));

            picoContainer.addComponent(handlerLocator);

            loadComponents(configJson.getJsonObject(app).getJsonArray("middleware-components"));

            // Create middleware queue we need for commandbus :
            Collection<IMiddleware> middleware = new ArrayList<>();
            middleware.add(picoContainer.getComponent(TransactionMiddleware.class));
            middleware.add(picoContainer.getComponent(HandlerMiddleware.class));

            // Create the command bus service and register it.-
            picoContainer.addComponent(new CommandBus(middleware));

            LOG.info("---------------------------  [SERVER INFO: START]  ---------------------------");
            picoContainer.start();
        }
    }

    /**
     * For all components, add Component to picoContainer.
     * @param array JsonArray from configuration file.
     */
    private void loadComponents(JsonArray array) {
        array.forEach(component -> {
            JsonObject current = (JsonObject) component;
            String className = current.getString("class-name");

            try {
                if (current.containsKey("factory-type") && current.containsKey("factory-method")) {
                    String factoryType = current.getString("factory-type");
                    String factoryMethod = current.getString("factory-method");

                    Class<?> factoryClass = Class.forName(factoryType);
                    Object factoryInstance = picoContainer.getComponent(factoryClass);

                    Method method = factoryClass.getMethod(factoryMethod);
                    Object instance = method.invoke(factoryInstance);

                    picoContainer.addComponent(instance);
                } else {
                    Class<?> clazz = Class.forName(className);
                    if (current.containsKey("params")) {
                        JsonArray params = current.getJsonArray("params");
                        Object[] constructorArgs = new Object[params.size()];

                        for (int i = 0; i < params.size(); i++) {
                            JsonObject param = params.getJsonObject(i);
                            constructorArgs[i] = param.getString("value");
                        }

                        picoContainer.addComponent(clazz, clazz.getConstructor(String.class, String.class, String.class, String.class).newInstance(constructorArgs));
                    } else {
                        picoContainer.addComponent(clazz);
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Unique endpoint simulate API queries or mutations, just like GraphQL.
     * @param resource String simulate a path like '/companie'.
     * @param command String simulate a method get, create, delete, update like a graphQL keyword.
     * @param params Object (json) simulate the body sent
     * @return serialized Object
     */
    public Object processRequest(String resource, String command, Map<String, Object> params) {
        try {
            return switch (resource) {
                case "company" -> dispatchCompanyResource(command, params);
                case "flight" -> dispatchFlightResource(command, params);
                case "baggage" -> dispatchBaggageResource(command, params);
                case "flightBusiness", "baggagesBusiness" -> dispatch(command, params);
                default -> throw new CommandNotFoundException(resource + " does not exist.");
            };

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }

    /*
    ----
    All these method can be merged into one, we'll keep it this way to ensure a great lisibility and respect the processRequest signature (for TP)
    ---
    */
    private Object dispatchCompanyResource(String command, Map<String, Object> params) {
        try {
            return switch (command) {
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
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }

    private Object dispatchFlightResource(String command, Map<String, Object> params) {
        try {
            return switch (command) {
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
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }
    private Object dispatchBaggageResource(String command, Map<String, Object> params) {
        try {
            return switch (command) {
                case "createBaggage" -> formatter.serializeObject(
                        this.getCommandBus().handle(new CreateBaggageCommand(
                                (String) params.get("id"),
                                (String) params.get("weight"),
                                (String) params.get("passenger")
                        ))
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
                case "deleteBaggage" -> formatter.serializeObject(
                        this.getCommandBus().handle(new DeleteBaggageCommand(
                                (String) params.get("id"),
                                (int) params.get("num")
                        ))
                );
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }

    private Object dispatch(String command, Map<String, Object> params) {
        try {
            return switch (command) {
                case "deliver" -> formatter.serializeObject(
                        this.getCommandBus().handle(new DeliverBaggageCommand(
                                (String) params.get("id"),
                                (int) params.get("num")
                        ))
                );
                case "retrieval" -> formatter.serializeObject(
                        this.getCommandBus().handle(new RetrievalBaggageCommand(
                                (String) params.get("id"),
                                (int) params.get("num")
                        ))
                );
                case "closeShipment" -> formatter.serializeObject(
                        this.getCommandBus().handle(new CloseShipmentCommand(
                                (String) params.get("id")
                        ))
                );
                case "getLostBaggages" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetLostBaggagesCommand(
                                (String) params.get("id")
                        ))
                );
                case "getUnclaimedBaggages" -> formatter.serializeObject(
                        this.getCommandBus().handle(new GetUnclaimedBaggagesCommand(
                                (String) params.get("id")
                        ))
                );
                default -> throw new CommandNotFoundException(command + " does not exist.");
            };

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }

    /**
     * get CommandBus from current container.
     * @return CommandBus
     */
    private CommandBus getCommandBus() {
        return picoContainer.getComponent(CommandBus.class);
    }

    /**
     * get EntityManager from current container.
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return picoContainer.getComponent(EntityManager.class);
    }
}

