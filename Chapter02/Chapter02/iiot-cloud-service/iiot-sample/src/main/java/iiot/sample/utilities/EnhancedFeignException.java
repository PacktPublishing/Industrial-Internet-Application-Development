package iiot.sample.utilities;

import feign.Response;
import iiot.sample.domain.persistence.entity.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 212568770 on 4/23/17.
 */
@Slf4j
public class EnhancedFeignException extends RuntimeException {
    @Getter
    private final String what;
    @Getter
    private final transient Response response;
    private final String msgBody;
    public EnhancedFeignException(String what, Response response){
        super();
        this.what = what;
        this.response = response;
        this.msgBody = readBody();
    }

    private String readBody(){
        InputStream stream = null;
        try {
            stream = response.body().asInputStream();
            return Util.readAsStringQuietly(stream);
        } catch (IOException e) {
            log.warn("", e);
        } finally {
            Util.closeQuietly(stream);
        }
        return null;
    }

    @Override
    public String getMessage() {
        return String.format("%s %s %s", getWhat(), this.response.reason(), msgBody);
    }
}


