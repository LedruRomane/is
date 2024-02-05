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
import tiw.is.server.service.ComponentLoader;
import tiw.is.server.utils.FixturesManager;
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
import tiw.is.server.utils.JsonFormatter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
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

        this.picoContainer = new PicoBuilder(new ConstructorInjection())
                .withCaching()
                .withLifecycle(new StartableLifecycleStrategy(new NullComponentMonitor()))
                .build();

        String configContent = new String(Files.readAllBytes(Paths.get("src/main/resources/appConfiguration.json")));

        try (JsonReader reader = Json.createReader(new StringReader(configContent))) {
            configJson = reader.readObject();

            loadComponents(configJson.getJsonObject(app).getJsonArray("persistence-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("data-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("handlers-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("commandbus-components"));

            LOG.info("---------------------------  [SERVER INFO: START]  ---------------------------");
            picoContainer.start();
        }
    }

    /**
     * For all components, add Components to picoContainer.
     * @param array JsonArray from configuration file.
     */
    private void loadComponents(JsonArray array) {
        array.forEach(component -> {
            ComponentLoader.load((JsonObject) component, this.picoContainer);
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

    /* --------------------  Dispatchers -------------------- */

    private Object dispatchCompanyResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create" -> formatter.serializeObject(
                    this.getCommandBus().handle(new CreateCompanyCommand((String) params.get("id")))
            );
            case "get" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetCompanyCommand((String) params.get("id")))
            );
            case "getAll" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetCompaniesCommand())
            );
            case "delete" -> formatter.serializeObject(
                    this.getCommandBus().handle(new DeleteCompanyCommand((String) params.get("id")))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    private Object dispatchFlightResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create", "update" -> formatter.serializeObject(
                    this.getCommandBus().handle(new CreateOrUpdateFlightCommand(
                            (String) params.get("id"),
                            (String) params.get("companyId"),
                            (String) params.get("pointLivraisonBagages")
                    ))
            );
            case "getAll" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetFlightsCommand())
            );
            case "get" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetFlightCommand((String) params.get("id")))
            );
            case "delete" -> formatter.serializeObject(
                    this.getCommandBus().handle(new DeleteFlightCommand((String) params.get("id")))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    private Object dispatchBaggageResource(String command, Map<String, Object> params) throws Exception {
        return switch (command) {
            case "create" -> formatter.serializeObject(
                    this.getCommandBus().handle(new CreateBaggageCommand(
                            (String) params.get("id"),
                            (String) params.get("weight"),
                            (String) params.get("passenger")
                    ))
            );
            case "getAll" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetBaggagesCommand())
            );
            case "get" -> formatter.serializeObject(
                    this.getCommandBus().handle(new GetBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            case "delete" -> formatter.serializeObject(
                    this.getCommandBus().handle(new DeleteBaggageCommand(
                            (String) params.get("id"),
                            (int) params.get("num")
                    ))
            );
            default -> throw new CommandNotFoundException(command + " does not exist.");
        };
    }

    private Object dispatch(String command, Map<String, Object> params) throws Exception {
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
    }

    /**
     * get CommandBus from current container.
     * @return CommandBus
     */
    private CommandBus getCommandBus() {
        return picoContainer.getComponent(CommandBus.class);
    }

    /**
     * Test purpose.
     * @throws Exception
     */
    public void resetDatabase() throws Exception {
        FixturesManager.resetDatabase(picoContainer.getComponent(EntityManager.class));
    }
}

