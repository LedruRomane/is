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
import tiw.is.server.service.IDispatcher;
import tiw.is.server.utils.FixturesManager;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ServeurImpl implements Serveur {

    private final MutablePicoContainer picoContainer;
    private final Logger log = LoggerFactory.getLogger(ServeurImpl.class);


    /**
     * Constructor Server, implement container and provide services & components.
     */
    public ServeurImpl(Path pathConfigurationFile) throws IOException {
        String app = "application-config";
        JsonObject configJson;

        this.picoContainer = new PicoBuilder(new ConstructorInjection())
                .withCaching()
                .withLifecycle(new StartableLifecycleStrategy(new NullComponentMonitor()))
                .build();

        String configContent = new String(Files.readAllBytes(pathConfigurationFile));

        try (JsonReader reader = Json.createReader(new StringReader(configContent))) {
            configJson = reader.readObject();

            loadComponents(configJson.getJsonObject(app).getJsonArray("persistence-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("data-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("handlers-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("commandbus-components"));
            loadComponents(configJson.getJsonObject(app).getJsonArray("dispatcher-components"));

            // Functional tests database's fixture manager.
            picoContainer.addComponent(FixturesManager.class);

            log.info("---------------------------  [SERVER INFO: START]  ---------------------------");
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
            IDispatcher dispatcher = picoContainer.getComponent(IDispatcher.class);
            return dispatcher.dispatch(resource, command, params);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "KO"; // Simulate an http error return.
        }
    }
    public MutablePicoContainer getContainer() {
        return picoContainer;
    }
}
