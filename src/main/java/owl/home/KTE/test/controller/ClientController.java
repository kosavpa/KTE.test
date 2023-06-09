package owl.home.KTE.test.controller;
/**
 * Rest сервис клиентов
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;
import owl.home.KTE.test.service.Interface.ClientService;

import java.util.List;


@RestController
@RequestMapping("/rest/v1/client-service")
public class ClientController {
    private ClientService service;

    @Autowired
    public void setService(ClientService service) {
        this.service = service;
    }

    /**
     * @return - список всех клиентов
     */
    @GetMapping("/all")
    ResponseEntity<List<Client>> getAllClient(){
        return ResponseEntity.ok(service.allClient());
    }

    /**
     * Обновление персональных скидок
     * @param clientId - идентификатор клиента
     * @param discount1 - скидка №1
     * @param discount2 - скидка №2
     * @return - клиент
     */
    @PatchMapping("/update-discounts/{clientId}/{discount1}/{discount2}")
    ResponseEntity<Client> updateDiscount(
            @PathVariable("clientId") long clientId,
            @PathVariable("discount1") int discount1,
            @PathVariable("discount2") int discount2){
        return ResponseEntity.ok(service.updateDiscounts(clientId, discount1, discount2));
    }

    /**
     * @param clientId - идентификатор клиента
     * @return - статистика клиента
     */
    @GetMapping("/statistic/{clientId}")
    ResponseEntity<StatisticClientResponse> clientStatistic(@PathVariable("clientId") long clientId){
        return ResponseEntity.ok(service.statisticClient(clientId));
    }
}
