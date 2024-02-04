package tiw.is.server.service;

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

public class BaggageBusinessTest extends FixturesManager {
    private static ServeurImpl serveurImpl;
    private final static Logger LOG = LoggerFactory.getLogger(BaggageBusinessTest.class);
    private final String resource = "baggagesBusiness";

    @BeforeAll
    public static void setupClass() throws IOException {
        serveurImpl = new ServeurImpl();
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        resetDatabase(serveurImpl.getEntityManager());
    }

    @Test
    void deliver() {
        String command = "deliver";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol1\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Paul\"}", result);

        // todo: assert isDelivre true
    }

    @Test
    void retrieval() {
        String command = "retrieval";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol2");
        params.put("num", 21);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"flightId\":\"vol2\",\"numero\":21,\"weight\":2.0,\"passenger\":\"Jack\"}", result);

        // todo: assert isRetrieval true
    }
}
