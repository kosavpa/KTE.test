package owl.home.KTE.test.service.util;

import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.util.StatisticClientResponse;
import test_web_service.kte.StatisticClient;

import java.util.List;
import java.util.stream.Collectors;

public class ClientUtil {

    public static double totalChecksPriceFromCheckList(List<Check> checkList){
        return checkList
                .stream()
                .mapToDouble(check -> check
                        .getShoppingList()
                        .stream()
                        .mapToDouble(product -> (product.getAmountProduct() * product.getProduct().getPrice()))
                        .sum())
                .sum();
    }

    public static int totalCheksDiscountFromChekList(List<Check> checkList){
        return checkList
                .stream()
                .mapToInt(check -> check
                        .getShoppingList()
                        .stream()
                        .mapToInt(ProductForCheck::getSumDiscount)
                        .sum())
                .sum();
    }

    public static List<test_web_service.kte.Client> mapClientListToWsClientList(List<Client> clients){
        return clients
                .stream()
                .map(client -> {
                    test_web_service.kte.Client wsClient = new test_web_service.kte.Client();

                    wsClient.setId(client.getId());
                    wsClient.setName(client.getName());
                    wsClient.setPersonalDiscount1(client.getPersonalDiscount1());
                    wsClient.setPersonalDiscount2(client.getPersonalDiscount2());

                    return wsClient;})
                .collect(Collectors.toList());
    }

    public static test_web_service.kte.Client mapClientToWsClient(Client client){
        test_web_service.kte.Client wsClient = new test_web_service.kte.Client();

        wsClient.setId(client.getId());
        wsClient.setName(client.getName());
        wsClient.setPersonalDiscount1(client.getPersonalDiscount1());
        wsClient.setPersonalDiscount2(client.getPersonalDiscount2());

        return wsClient;
    }

    public static StatisticClient mapStatisticClientToWsStatisticClient(StatisticClientResponse statisticClient){
        StatisticClient wsStatisticClient = new StatisticClient();

        wsStatisticClient.setClient(mapClientToWsClient(statisticClient.getClient()));
        wsStatisticClient.setAmountCheck(statisticClient.getAmountCheck());
        wsStatisticClient.setTotalCheckDiscountSum(statisticClient.getTotalCheckDiscountSum());
        wsStatisticClient.setTotalCheckSum(statisticClient.getTotalCheckSum());

        return wsStatisticClient;
    }
}
