package tiw.is.server.resource;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.db.ServeurManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaggageTest extends ServeurManager {
    private final static Logger LOG = LoggerFactory.getLogger(BaggageTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();
    private final String resource = "baggage";

    @Test
    void createBaggage() {
        String command = "create";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol2");
        params.put("weight", "8");
        params.put("passenger", "Muse");

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol2\",\"numero\":22,\"weight\":8.0,\"passenger\":\"Muse\"}", result);
    }

    @Test
    void getBaggages() {
        String command = "getAll";

        String result = (String) serveur.processRequest(resource, command, voidParams);
        LOG.info(result);
        assertEquals("[{\"flightId\":\"vol1\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Paul\"},{\"flightId\":\"vol2\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Jack\"},{\"flightId\":\"vol3\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Foo\"},{\"flightId\":\"vol3\",\"numero\":22,\"weight\":2.0,\"passenger\":\"Unclaimed\"},{\"flightId\":\"vol3\",\"numero\":23,\"weight\":2.0,\"passenger\":\"Lost\"},{\"flightId\":\"vol4\",\"numero\":21,\"weight\":2.0,\"passenger\":\"John\"}]", result);
    }

    @Test
    void getBaggage() {
        String command = "get";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol1\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Paul\"}", result);
    }

    @Test
    void deleteBaggage() {
        String command = "delete";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("true", result);
    }

}
