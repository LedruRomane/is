package tiw.is.server.resource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.db.ServeurManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CompanyTest extends ServeurManager {
    private final static Logger LOG = LoggerFactory.getLogger(CompanyTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();
    private final String resource = "company";

    private final String companyID = "company1";

    @Test
    void createCompany() {
        String command = "create";
        Map<String, Object> params = new HashMap<>();
        String newID = "c-new";
        params.put("id", newID);

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"" + newID + "\"}", result);
    }

    @Test
    void getCompany() {
        String command = "get";
        Map<String, Object> params = new HashMap<>();
        params.put("id", this.companyID);

        String result = (String) serveur.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"" + this.companyID + "\"}", result);
    }

    @Test
    void getCompanies() {
        String command = "getAll";

        String result = (String) serveur.processRequest(resource, command, voidParams);
        LOG.info(result);
        assertEquals("[{\"id\":\"company1\"},{\"id\":\"company2\"}]", result);
    }

    @Test
    void deleteCompany() {
        String command = "delete";
        Map<String, Object> params = new HashMap<>();
        params.put("id", this.companyID);

        String result = (String) serveur.processRequest(resource, command, params);

        LOG.info(result);
        assertEquals("true", result);
    }
}