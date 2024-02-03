package tiw.is.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.db.FixturesManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaggageTest extends FixturesManager {
    private static ServeurImpl serveurImpl;
    private final static Logger LOG = LoggerFactory.getLogger(BaggageTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();


    @BeforeAll
    public static void setupClass() {
        serveurImpl = new ServeurImpl();
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        resetDatabase(serveurImpl.getEntityManager());
    }

    @Test
    void getBagages() {
        String command = "getBaggages";

        String result = (String) serveurImpl.processRequest(command, voidParams);
        LOG.info(result);
        assertEquals("[{\"volId\":\"vol1\",\"numero\":21,\"poids\":2.0,\"passager\":\"Paul\"},{\"volId\":\"vol2\",\"numero\":21,\"poids\":2.0,\"passager\":\"Jack\"},{\"volId\":\"vol3\",\"numero\":21,\"poids\":2.0,\"passager\":\"Foo\"},{\"volId\":\"vol3\",\"numero\":22,\"poids\":2.0,\"passager\":\"Doh\"},{\"volId\":\"vol4\",\"numero\":21,\"poids\":2.0,\"passager\":\"John\"}]", result);
    }

    @Test
    void getBagage() {
        String command = "getBaggage";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("num", 21);

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("{\"volId\":\"vol1\",\"numero\":21,\"poids\":2.0,\"passager\":\"Paul\"}", result);
    }

}
