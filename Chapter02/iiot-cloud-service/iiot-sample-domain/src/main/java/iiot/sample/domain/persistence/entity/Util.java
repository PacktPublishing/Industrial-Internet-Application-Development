package iiot.sample.domain.persistence.entity;

import iiot.sample.domain.error.ErrorCode;
import iiot.sample.domain.error.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by 212568770 on 4/15/17.
 */
@Slf4j
public class Util {
    private Util(){
    }

    public static void notNull(Object o, ErrorCode errorCode, String message) throws ServiceException {
        if (o == null) {
            throw new ServiceException(errorCode, message);
        }
    }

    public static void notBadAssetUuid(String uuid, ErrorCode errorCode, String message) throws ServiceException {
        if (uuid != null && uuid.length() > 2000){
            throw new ServiceException(errorCode, message);
        }

        if (!uuid.matches("[/a-zA-Z0-9-]*")){
            throw new ServiceException(errorCode, message);
        }
    }

    public static void notBadString(String s, ErrorCode errorCode, String message) throws ServiceException {
        if (s != null && s.length() > 100){
            throw new ServiceException(errorCode, message);
        }

        if (!s.matches("[a-zA-Z0-9-_]*")){
            throw new ServiceException(errorCode, message);
        }
    }


    public static String readResourceAsString(String resourcePath){
        InputStream inputStream = null;
        try {
            inputStream = Util.class.getResourceAsStream(resourcePath);
            return readAsString(inputStream);
        } catch (IOException e){
            log.error("Missing resourcePath", e);
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    public static String readAsString(InputStream inputStream) throws IOException{
        if (inputStream == null){
            return null;
        }
        byte[] data = new byte[50000];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        int len = -1;
        while ((len = inputStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        return new String(outStream.toByteArray());
    }

    public static String readAsStringQuietly(InputStream inputStream){
        try {
            return readAsString(inputStream);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    public static final void closeQuietly(InputStream inputStream){
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                log.info("failed to close stream", e);
            }
        }
    }


    public static String read(File file) throws IOException {
        try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            return readAsString(stream);
        }
    }


}
