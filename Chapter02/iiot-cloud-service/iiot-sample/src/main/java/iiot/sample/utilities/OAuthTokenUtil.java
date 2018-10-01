package iiot.sample.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by brandonfeist on 3/21/17.
 */
@Component
public class OAuthTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(OAuthTokenUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String TENANT_HEADER_NAME = "Acs-Zone-Subdomain";

    private OAuthTokenUtil(){

    }

    public static Map<String, Object> parseToken(String itoken) {
       // Jwt tokenJwt = null;
        Map<String, Object> claims = null;

      /*  try {
            if(itoken != null) {
                String token = itoken.contains(" ") ? itoken.split(" ")[1].trim() : itoken;
                tokenJwt = JwtHelper.decode(token);

                if (tokenJwt != null) {
                    tokenJwt.getClaims();
                    claims = objectMapper.readValue(tokenJwt.getClaims(), new TypeReference<Map<String, Object>>() {});
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing token::" + itoken, e);
        }*/

        return claims;
    }

    //Get origin from the token
    public static String getOrigin(Map<String, ?> token ) {
        String origin = null;

        if(token != null && token.containsKey("origin")) {
            origin = token.get("origin").toString();
        }

        return origin;
    }


    public static String getZoneUuid(Map<String, ?> token ) {
        String zoneUuid = null;

        if (token != null && token.containsKey("zid")) {
            zoneUuid = token.get("zid").toString();
        }

        return zoneUuid;
    }
}
