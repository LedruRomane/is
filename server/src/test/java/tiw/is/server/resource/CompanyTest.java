package tiw.is.server.resource;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.ServeurImpl;
import tiw.is.server.db.FixturesManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class CompanyTest extends FixturesManager {
    private static ServeurImpl serveurImpl;
    private final static Logger LOG = LoggerFactory.getLogger(CompanyTest.class);
    private final Map<String, Object> voidParams = new HashMap<>();
    private final String resource = "company";

    private final String companyID = "company1";


    @BeforeAll
    public static void setupClass() throws IOException {
        serveurImpl = new ServeurImpl();
    }

    @BeforeEach
    public void setupDatabase() throws Exception {
        resetDatabase(serveurImpl.getEntityManager());
    }

    @Test
    void createCompany() {
        String command = "createCompany";
        Map<String, Object> params = new HashMap<>();
        String newID = "c-new";
        params.put("id", newID);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\""+ newID +"\"}", result);
    }

    @Test
    void getCompany() {
        String command = "getCompany";
        Map<String, Object> params = new HashMap<>();
        params.put("id", this.companyID);

        String result = (String) serveurImpl.processRequest(resource, command, params);
        LOG.info(result);
        assertEquals("{\"id\":\""+ this.companyID +"\"}", result);
    }

    @Test
    void getCompanies() {
        String command = "getCompanies";

        String result = (String) serveurImpl.processRequest(resource, command, voidParams);
        LOG.info(result);
        assertEquals("[{\"id\":\"company1\"},{\"id\":\"company2\"}]", result);
    }

    @Test
    void deleteCompany() {
        String command = "deleteCompany";
        Map<String, Object> params = new HashMap<>();
        params.put("id", this.companyID);

        String result = (String) serveurImpl.processRequest(resource, command, params);

        LOG.info(result);
        assertEquals("true", result);
        //todo: assert the flight isn't in database anymore ?
    }

    // todo: test exceptions ?
}