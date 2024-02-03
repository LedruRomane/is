package tiw.is.server.resource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.ServeurImpl;
import tiw.is.server.db.FixturesManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightTest extends FixturesManager {
    private static ServeurImpl serveurImpl;
    private final static Logger LOG = LoggerFactory.getLogger(FlightTest.class);
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
    void createFlight() {
        String command = "createFlight";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "v-new");
        params.put("companyId", "company1");
        params.put("pointLivraisonBagages", "Guéret");

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"v-new\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Guéret\"}", result);
    }

    @Test
    void updateFlight() {
        String command = "updateFlight";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("companyId", "company2");
        params.put("pointLivraisonBagages", "Pétaouchnok");

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"vol1\",\"company\":\"company2\",\"pointLivraisonBagages\":\"Pétaouchnok\"}", result);
    }

    @Test
    void getFlights() {
        String command = "getFlights";

        String result = (String) serveurImpl.processRequest(command, voidParams);
        LOG.info(result);
        assertEquals("[{\"id\":\"vol1\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Paris\"},{\"id\":\"vol2\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Lyon\"},{\"id\":\"vol3\",\"company\":\"company2\",\"pointLivraisonBagages\":\"Budapest\"},{\"id\":\"vol4\",\"company\":\"company2\",\"pointLivraisonBagages\":\"London\"}]", result);
    }

    @Test
    void getFlight() {
        String command = "getFlight";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"vol1\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Paris\"}", result);
    }

    @Test
    void deleteFlight() {
        String command = "deleteFlight";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("true", result);
        //todo: assert the flight isn't in database anymore ?
    }

    //todo: assert exceptions.
}
