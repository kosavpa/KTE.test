package owl.home.KTE.test.webService.Interface;


import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;

import javax.jws.WebParam;
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
            className = "owl.home.KTE.test.webservice.AllClientRequest")
    @ResponseWrapper(
            localName = "getAllClientResponse",
            className = "owl.home.KTE.test.webservice.AllClientResponse")
    List<Client> getAllClient();

    @WebResult(name = "ClientStatistic")
    @RequestWrapper(
            localName = "getClientStatisticRequest",
            className = "owl.home.KTE.test.webservice.ClientStatisticRequest")
    @ResponseWrapper(
            localName = "getClientStatisticResponse",
            className = "owl.home.KTE.test.webservice.ClientStatisticResponse")
    StatisticClientResponse getStatisticClient(@WebParam(name = "clientId") long clientId);
}