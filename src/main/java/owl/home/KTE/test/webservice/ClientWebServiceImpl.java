package owl.home.KTE.test.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.service.Interface.ClientService;

import javax.jws.WebService;
import java.util.List;


@Component
@WebService(serviceName = "ClientService", targetNamespace = "http://kte.test-web-service")
public class ClientWebServiceImpl implements ClientWebService{
    @Autowired
    ClientService service;

    @Override
    public List<Client> getAllClient() {
        return service.allClient();
    }

//    @Override
//    public StatisticClientResponse getStatisticClient(long clientId) {
//        return null;
//    }
}
