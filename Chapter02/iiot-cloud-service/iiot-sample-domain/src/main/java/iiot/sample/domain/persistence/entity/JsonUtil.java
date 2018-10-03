package iiot.sample.domain.persistence.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * Created by 212568770 on 4/2/17.
 */
@Slf4j
public class JsonUtil {
    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }


    public static Map<String, Object> asMapQuietly(Object jsonNode) {
        String jsonStr;
        if (jsonNode instanceof String){
            jsonStr = (String) jsonNode;
        } else {
            jsonStr = asStringQuitely(jsonNode);
        }
        if (jsonStr != null) {
            try {
                return mapper.readValue(jsonStr, Map.class);
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return null;
    }

    /**
     * Convert jsonNode object to string without throwing a parse exception
     *
     * @param jsonNode
     * @return null on failure
     */
    public static String asStringQuitely(Object jsonNode) {
        if (jsonNode != null) {
            try {
                return mapper.writeValueAsString(jsonNode);
            } catch (JsonProcessingException e) {
                log.warn("", e);
            }
        }
        return null;
    }

    /**
     * convert String json to JsonNode without throwing an error
     *
     * @param jsonStr
     * @return null on failure
     */
    public static JsonNode toJsonNodeQuietly(String jsonStr) {
        if (jsonStr != null) {
            try {
                return mapper.readTree(jsonStr);
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return null;
    }


    /**
     * Converts jsonString to an object of a type without throwing an error
     * @param jsonStr
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> T toTypedObjectQuietly(String jsonStr, Class<T> klass) {
        if (jsonStr != null) {
            try {
                return mapper.readValue(jsonStr, klass);
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return null;
    }


    /**
     * returns Json object for jsonStr
     *
     * @param jsonStr
     * @return json object
     * @throws IOException
     */
    public static JsonNode toJsonNode(String jsonStr) throws IOException {
        if (jsonStr == null) {
            throw new IOException("null jsonStr");
        }
        return mapper.readTree(jsonStr);
    }

    public static String asString(JsonNode jsonNode) throws IOException {
        return mapper.writeValueAsString(jsonNode);

    }

    public static String getJsonBodyFromFile(String fileURL) {
        InputStream stream = JsonUtil.class.getResourceAsStream(fileURL);
        String json = "";
        try {
            JsonNode jsonNode = mapper.readTree(stream);
            json = jsonNode.toString();
        } catch (IOException e) {
            log.warn("", e);
        }
        return json;
    }


    public static Set<String> toSetQuietly(String jsonStr) {
        if (jsonStr != null) {
            try {
                return mapper.readValue(jsonStr, Set.class);
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return null;
    }

}
