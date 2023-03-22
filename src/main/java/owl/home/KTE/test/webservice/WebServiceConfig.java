package owl.home.KTE.test.webservice;


import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;


@Configuration
public class WebServiceConfig {
    @Autowired
    Bus bus;

    @Bean
    public Endpoint clientEndpoint(ClientWebServiceImpl clientWebService){
        EndpointImpl endpoint = new EndpointImpl(bus, clientWebService);
        endpoint.publish("/ClientService");

        return endpoint;
    }
}

