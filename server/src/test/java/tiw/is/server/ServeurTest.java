package tiw.is.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiw.is.server.utils.JsonFormatter;
import tiw.is.vols.livraison.exception.ResourceAlreadyExistsException;
import tiw.is.vols.livraison.model.Compagnie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServeurTest {
    private static ServeurImpl serveurImpl;
    private static JsonFormatter compagnieFormatter;

    private final static Logger LOG = LoggerFactory.getLogger(ServeurTest.class);

    @BeforeAll
    static void setUp() throws SQLException {

        serveurImpl = new ServeurImpl();
        compagnieFormatter = new JsonFormatter<Compagnie>();
    }

    @Test
    public void CreateCompagnie() throws IOException, ResourceAlreadyExistsException {

        String command = "createCompagnie";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "compagnie1");

        String result = (String) serveurImpl.processRequest(command, params);
        assertSame("compagnie1", result);

        //todo: ask if I assert the item was correctly created in database ?
    }
}