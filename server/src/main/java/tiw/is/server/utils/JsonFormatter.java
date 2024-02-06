package tiw.is.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonFormatter<T> {
    private final ObjectMapper mapper = new ObjectMapper();

    public String serializeObject(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public T deserializeJson(String jsonString, Class<T> myclass) throws IOException {
        return mapper.readValue(jsonString, myclass);
    }
}