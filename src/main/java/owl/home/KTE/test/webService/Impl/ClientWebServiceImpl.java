package owl.home.KTE.test.webService.Impl;
/**
 * Имплиментация веб службы клиентов
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.webService.Interface.ClientWebService;

import javax.jws.WebService;
import java.util.List;


@Component
@WebService(serviceName = "ClientService", targetNamespace = "http://kte.test-web-service")
public class ClientWebServiceImpl implements ClientWebService {
    ClientService service;

    @Autowired
    public void setService(ClientService service) {
        this.service = service;
    }

    @Override
    public List<Client> getAllClient() {
        return service.allClient();
    }

    @Override
    public StatisticClientResponse getStatisticClient(long clientId) {
        return service.statisticClient(clientId);
    }

    @Override
    public Client updateDiscount(long clientId, int discount1, int discount2) {
        return service.updateDiscounts(clientId, discount1, discount2);
    }
}