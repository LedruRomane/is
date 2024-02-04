package tiw.is.server.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.ServeurImpl;
import tiw.is.server.db.FixturesManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaggageTest extends FixturesManager {
    private static ServeurImpl serveurImpl;
    private final static Logger LOG = LoggerFactory.getLogger(BaggageTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();
    private final String resource = "baggage";


    @BeforeAll
    public static void setupClass() throws IOException {
        serveurImpl = new ServeurImpl();
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        resetDatabase(serveurImpl.getEntityManager());
    }

    @Test
    void createBaggage() {
        //todo: assert count baggage 5
        String command = "createBaggage";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol2");
        params.put("weight", "8");
        params.put("passenger", "Muse");

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol2\",\"numero\":22,\"weight\":8.0,\"passenger\":\"Muse\"}", result);

        // todo: assert count baggage +1 = 6
    }

    @Test
    void getBaggages() {
        String command = "getBaggages";

        String result = (String) serveurImpl.processRequest(resource, command, voidParams);
        LOG.info(result);
        assertEquals("[{\"flightId\":\"vol1\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Paul\"},{\"flightId\":\"vol2\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Jack\"},{\"flightId\":\"vol3\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Foo\"},{\"flightId\":\"vol3\",\"numero\":22,\"weight\":2.0,\"passenger\":\"Unclaimed\"},{\"flightId\":\"vol3\",\"numero\":23,\"weight\":2.0,\"passenger\":\"Lost\"},{\"flightId\":\"vol4\",\"numero\":21,\"weight\":2.0,\"passenger\":\"John\"}]", result);
    }

    @Test
    void getBaggage() {
        String command = "getBaggage";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol1\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Paul\"}", result);
    }

    @Test
    void deleteBaggage() {
        String command = "deleteBaggage";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("true", result);
    }

}
