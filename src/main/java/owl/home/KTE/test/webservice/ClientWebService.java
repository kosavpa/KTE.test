package owl.home.KTE.test.webservice;


import owl.home.KTE.test.model.client.Client;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;


@WebService(name = "ClientService" ,targetNamespace = "http://kte.test-web-service")
public interface ClientWebService {
    @WebResult(name = "Client")
    @RequestWrapper(
            localName = "getAllClientRequest",
            className = "owl.home.KTE.test.webservice.ClientWebServiceRequest")
    @ResponseWrapper(
            localName = "getAllClientResponse",
            className = "owl.home.KTE.test.webservice.ClientWebServiceResponce")
    List<Client> getAllClient();
//    StatisticClientResponse getStatisticClient(long clientId);
}
