package iiot.sample.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 212561830 on 7/25/16.
 */
@Component
public class SmartProxySelector extends ProxySelector {
    private static final Logger logger = LoggerFactory.getLogger(SmartProxySelector.class);

    @Value("${PROXY_ENABLED:false}" )
    Boolean proxyEnabled;
    private List<Proxy> proxies = new ArrayList<>();

    public SmartProxySelector() {
      // spring requirement
    }

   @PostConstruct
    public void initialize(){
        if(proxyEnabled) {
            String httpProxyHost = System.getenv().get("HTTP_PROXY_HOST");
            int httpProxyPort = Integer.parseInt(System.getenv().get("HTTP_PROXY_PORT"));
            proxies.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, httpProxyPort)));
        }
       enableProxy();
    }

    @Override
    public List<Proxy> select(URI uri) {
        logger.debug("URI {} ", uri);
        if(uri.getHost().contains("localhost")){
            List<Proxy> responseList = new ArrayList<>();
            responseList.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(uri.getHost(), uri.getPort())));
            return responseList;
        }else{
            return proxies;
        }
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        //do nothing on proxy connection failure
    }

    public void enableProxy(){
        if (proxyEnabled){
            ProxySelector.setDefault(this);
        }
    }

}
