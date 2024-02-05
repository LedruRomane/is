package tiw.is.server;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;
import org.picocontainer.monitors.NullComponentMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.service.ComponentLoader;
import tiw.is.server.service.Dispatcher;
import tiw.is.server.utils.FixturesManager;
import tiw.is.vols.livraison.exception.CommandNotFoundException;
import tiw.is.vols.livraison.infrastructure.commandBus.CommandBus;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;
    private final static Logger LOG = LoggerFactory.getLogger(ServeurImpl.class);


    /**
     * Constructor Server, implement container and provide services & components.
     */
    public ServeurImpl() throws IOException {
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

            // Client test fixture manager.
            picoContainer.addComponent(FixturesManager.class);

            LOG.info("---------------------------  [SERVER INFO: START]  ---------------------------");
            picoContainer.start();
        }
    }

    /**
     * For all components, add Components to picoContainer.
     *
     * @param array JsonArray from configuration file.
     */
    private void loadComponents(JsonArray array) {
        array.forEach(component -> ComponentLoader.load((JsonObject) component, this.picoContainer));
    }

    /**
     * Unique endpoint simulate API queries or mutations, just like GraphQL.
     *
     * @param resource String simulate a path like '/companie'.
     * @param command  String simulate a method get, create, delete, update like a graphQL keyword.
     * @param params   Object (json) simulate the body sent
     * @return serialized Object
     */
    public Object processRequest(String resource, String command, Map<String, Object> params) {
        try {
            return switch (resource) {
                case "company" -> Dispatcher.dispatchCompanyResource(command, params, picoContainer.getComponent(CommandBus.class));
                case "flight" -> Dispatcher.dispatchFlightResource(command, params, picoContainer.getComponent(CommandBus.class));
                case "baggage" -> Dispatcher.dispatchBaggageResource(command, params, picoContainer.getComponent(CommandBus.class));
                case "flightBusiness", "baggagesBusiness" -> Dispatcher.dispatch(command, params, picoContainer.getComponent(CommandBus.class));
                default -> throw new CommandNotFoundException(resource + " does not exist.");
            };

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }
    public MutablePicoContainer getContainer() {
        return picoContainer;
    }
}
