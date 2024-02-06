package tiw.is.server;

import org.picocontainer.MutablePicoContainer;

import java.util.Map;

public interface Serveur {
    public Object processRequest(String resource, String command, Map<String, Object> params);

    public MutablePicoContainer getContainer();
}