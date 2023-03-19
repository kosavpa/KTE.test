package owl.home.KTE.test.service.Interface;


import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;

import java.util.List;


@Service
public interface ClientService {
    Client clientById(Long clientId);
    boolean deleteClientById(Long clientId);
    Client saveClient(Client client);
    List<Client> allClient();
    StatisticClientResponse statisticClient(long clientId);
    Client updateDiscounts(long clientId, int discount1, int discount2);
}