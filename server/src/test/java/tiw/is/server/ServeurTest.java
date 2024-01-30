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
    public void CreateCompagie() throws IOException, ResourceAlreadyExistsException {
        String command = "createCompagnies";

        String json1 = "{\"id\":\"compagnie1\"}";
        String json2 = "{\"id\":\"compagnie2\"}";

        Map<Integer, String> params = new HashMap<>();
        params.put(1, json1);
        params.put(2, json2);

        String result = (String) serveurImpl.processRequest(command, params);
        assert result.contains("compagnie1");
        assert result.contains("compagnie2");
    }
}