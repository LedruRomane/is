package tiw.is.server;

import java.util.Map;

public interface Serveur {
    public Object processRequest(String resource, String command, Map<String, Object> params);

    public void resetDatabase() throws Exception;
}