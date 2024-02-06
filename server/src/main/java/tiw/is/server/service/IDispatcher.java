package tiw.is.server.service;

import java.util.Map;

public interface IDispatcher {

    public Object dispatch(String resource, String command, Map<String, Object> params) throws Exception;
}
