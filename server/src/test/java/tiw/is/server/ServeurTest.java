package tiw.is.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServeurTest {
    private static ServeurImpl serveurImpl;

    private final static Logger LOG = LoggerFactory.getLogger(ServeurTest.class);

    @BeforeAll
    static void setUp() throws SQLException {

        serveurImpl = new ServeurImpl();
    }

    @Test
    public void CreateCompany() throws IOException, ResourceAlreadyExistsException {

        String command = "createCompany";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "compagnie1");

        String result = (String) serveurImpl.processRequest(command, params);
        LOG.info(result);
        assertEquals("{\"id\":\"compagnie1\"}", result);
    }
}