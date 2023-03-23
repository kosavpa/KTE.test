package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.util.StatisticClientResponse;
import owl.home.KTE.test.repo.ClientRepository;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ClientService;

import java.util.List;
import java.util.NoSuchElementException;

import static owl.home.KTE.test.service.util.ClientUtil.*;


@Component
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {
    private ClientRepository repository;
    private CheckService checkService;

    @Autowired
    public void setRepository(ClientRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public Client clientById(Long clientId) {
        return repository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Client with this id not found!")
        );
    }

    @Transactional
    @Override
    public boolean deleteClientById(Long clientId) {
        repository.deleteById(clientId);

        return !repository.existsById(clientId);
    }

    @Transactional
    @Override
    public Client saveClient(Client client) {
        return repository.save(client);
    }

    @Override
    public List<Client> allClient() {
        return repository.findAll();
    }

    @Override
    public StatisticClientResponse statisticClient(long clientId) {
        Client client = clientById(clientId);
        List<Check> checkList = checkService.checksByClientId(clientId);
        int totalDiscountSum = totalCheksDiscountFromChekList(checkList);
        double totalPriseSum = totalChecksPriceFromCheckList(checkList);

        return StatisticClientResponse
                .builder()
                .client(client)
                .amountCheck(checkList.size())
                .totalCheckSum(totalPriseSum)
                .totalCheckDiscountSum(totalDiscountSum)
                .build();
    }

    @Transactional
    @Override
    public Client updateDiscounts(long clientId, int discount1, int discount2) {
        Client oldClient = clientById(clientId);

        oldClient.setPersonalDiscount1(discount1);
        oldClient.setPersonalDiscount2(discount2);

        saveClient(oldClient);
        Client newClient = clientById(oldClient.getId());

        if(!oldClient.equals(newClient)){
            throw new IllegalArgumentException("Something went wrong!");
        }

        return newClient;
    }
}