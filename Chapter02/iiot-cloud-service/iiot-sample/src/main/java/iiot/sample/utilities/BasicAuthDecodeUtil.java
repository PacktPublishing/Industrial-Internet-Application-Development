package iiot.sample.utilities;

import java.util.Map;
import java.util.HashMap;

import java.util.Base64;


/**
 * Created by 212367835 on 8/15/17.
 */
public class BasicAuthDecodeUtil {

    public static String USERNAME="Username";
    public static String PWD="PWD";

    public static Map<String, String> decodeBasicAuth(String authorizationHeader){



        Map<String, String> claims = new HashMap<String, String>();
          if (authorizationHeader != null) {
            // *****Decode the authorisation String*****
              byte[] e = Base64.getDecoder().decode(authorizationHeader.substring(6));
            String usernpass = new String(e);
            // *****Split the username from the password*****
            claims.put(USERNAME, usernpass.substring(0, usernpass.indexOf(":")));
            claims.put(PWD,usernpass.substring(usernpass.indexOf(":") + 1));
            // check username and password
        }

        return  claims;
    }
}
