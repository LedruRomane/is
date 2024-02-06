package tiw.is.vols.livraison.functionnal.resource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.livraison.functionnal.db.ServeurManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightTest extends ServeurManager {
    private final static Logger LOG = LoggerFactory.getLogger(FlightTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();
    private final String resource = "flight";

    @Test
    void createFlight() {
        String command = "create";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "v-new");
        params.put("companyId", "company1");
        params.put("pointLivraisonBagages", "Guéret");

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"v-new\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Guéret\"}", result);
    }

    @Test
    void updateFlight() {
        String command = "update";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");
        params.put("companyId", "company2");
        params.put("pointLivraisonBagages", "Pétaouchnok");

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"vol1\",\"company\":\"company2\",\"pointLivraisonBagages\":\"Pétaouchnok\"}", result);
    }

    @Test
    void getFlights() {
        String command = "getAll";

        String result = (String) serveur.processRequest(resource, command, voidParams);
        LOG.info(result);
        assertEquals("[{\"id\":\"vol1\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Paris\"},{\"id\":\"vol2\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Lyon\"},{\"id\":\"vol3\",\"company\":\"company2\",\"pointLivraisonBagages\":\"Budapest\"},{\"id\":\"vol4\",\"company\":\"company2\",\"pointLivraisonBagages\":\"London\"}]", result);
    }

    @Test
    void getFlight() {
        String command = "get";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"vol1\",\"company\":\"company1\",\"pointLivraisonBagages\":\"Paris\"}", result);
    }

    @Test
    void deleteFlight() {
        String command = "delete";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "vol1");

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("true", result);
    }
}
