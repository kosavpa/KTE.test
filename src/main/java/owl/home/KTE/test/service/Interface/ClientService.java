package owl.home.KTE.test.service.Interface;
/**
 * Сервисный слой для клиентов
 */

import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;

import java.util.List;


@Service
public interface ClientService {
    /**
     * Поиск клиента по id
     * @param clientId - идентификатор клиента
     * @return - клиент
     */
    Client clientById(Long clientId);

    /**
     * Удаление клиента по id
     * @param clientId - идентификатор клиента
     * @return - статус операции (true - успех, false - провал)
     */
    boolean deleteClientById(Long clientId);

    /**
     * Сохранение клиента
     * @param client - идентификатор клиента
     * @return - клиент
     */
    Client saveClient(Client client);

    /**
     * Поиск клиентов
     * @return - список клиентов
     */
    List<Client> allClient();
    StatisticClientResponse statisticClient(long clientId);
    Client updateDiscounts(long clientId, int discount1, int discount2);
}