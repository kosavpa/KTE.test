package owl.home.KTE.test.service.util;
/**
 * Утильный класс для товара (напрмер преобразование сервлетного запроса в объект запроса итогой стоимости списка покупок и т.д.).
 */

import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.service.Interface.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;


public class ProductUtil {
    /**
     * Преобразование сервлетного запроса в список объект запроса итоговой стоимоти списка покупок.
     * Может бросать непроверяемое сообщение с соответствующим исключением
     * @param request - cthdktnysq pfghjc
     * @return список запросов итоговой стоимоти списка покупок
     */
    public static List<TotalPriceShopingListRequest> totalPriseRequestList(HttpServletRequest request){
        Map<String, String[]> paramMap = request.getParameterMap();
        String[] idsProducts = paramMap.get("id");
        String[] amountsProducts = paramMap.get("amount");

        if (idsProducts.length != amountsProducts.length)
            throw new IllegalArgumentException("Amount 'id' params no equals 'amount' params!");

        List<TotalPriceShopingListRequest> totalPriseRequestList = new LinkedList<>();
        for(int i = 0; i < idsProducts.length; i++){
            totalPriseRequestList.add(TotalPriceShopingListRequest
                    .builder()
                    .productId(Long.parseLong(idsProducts[i]))
                    .amount(Integer.parseInt(amountsProducts[i]))
                    .build());
        }

        return totalPriseRequestList;
    }

    /**
     * Стоимость с учетом скидки и количества
     * @param productPrice - стоимость одной единицы товара
     * @param productDiscount - скидка товара
     * @param productAmount - количество товара
     * @return - стоимость
     */
    public static double priceWithDiscountAndAmount(double productPrice, int productDiscount, int productAmount){

        return ((productPrice - productPrice * productDiscount / 100) * productAmount);
    }

    /**
     * Преобразование списка запросов итоговой стоимоти списка покупок в объект ответа итогой стоимости с учетом валюты
     * @param requestList - список запросов итоговой стоимоти
     * @param carrensy - валюта
     * @param productMap - hashmap товаров "id - product"
     * @return - объект ответа итогой стоимости
     */
    public static TotalPriceShopingListResponse totalPriceProductResponseFromRequestShopingList(
            List<TotalPriceShopingListRequest> requestList,
            Carrensy carrensy,
            Map<Long, Product> productMap){

        double totalPrice = requestList
                .stream()
                .mapToDouble(tpr ->
                        priceWithDiscountAndAmount(
                                productMap.get(tpr.getProductId()).getPrice(),
                                productMap.get(tpr.getProductId()).getDiscount(),
                                tpr.getAmount()))
                .sum();

        return TotalPriceShopingListResponse
                .builder()
                .totalPrice(carrensy.equals(Carrensy.RUB) ? totalPrice : totalPrice * 100)
                .carrensy(carrensy)
                .build();
    }

    /**
     * Итоговая стоимость во всех чеках для статистики (в рублях)
     * @param productForCheck - лист продукта ассоциированного с чеками
     * @return - стоимость товара во всех чеках
     */
    public static double totalPriceForStatisticFromShopingList(List<ProductForCheck> productForCheck){
        return productForCheck
                .stream()
                .mapToDouble(sl -> sl.getProduct().getPrice())
                .sum();
    }

    /**
     * Итоговая скидка во всех чеках для статистики
     * @param productForCheck - лист продукта ассоциированного с чеками
     * @return - скидка на товар во всех чеках
     */
    public static int totalDiscountForStatisticFromShopingList(List<ProductForCheck> productForCheck){
        return productForCheck
                .stream()
                .mapToInt(ProductForCheck::getSumDiscount)
                .sum();
    }

    /**
     * СОздание списка товаров для чека
     * @param shopingList - список запросов итоговой стоимоти
     * @param productMap - hashmap товаров "id - product"
     * @return - коллекцию товаров для чека
     */
    public static Set<ProductForCheck> productForCheckListFromTotalPriceShopingListRequest(
            List<TotalPriceShopingListRequest> shopingList,
            Map<Long, Product> productMap){
        return shopingList
                .stream()
                .map(tpslr -> {
                    Product product = productMap.get(tpslr.getProductId());
                    return ProductForCheck
                            .builder()
                            .product(product)
                            .amountProduct(tpslr.getAmount())
                            .sumDiscount(product.getDiscount())
                            .build();
                })
                .collect(Collectors.toSet());
    }

    /**
     * Стоимость с учётом клиентской скидки
     * @param client - клиент
     * @param productsForCheck - коллекцию товаров для чека
     * @return - стоимость
     */
    public static double priceWithClientDiscount(Client client, Set<ProductForCheck> productsForCheck){
        int productCount = productsForCheck
                .stream()
                .mapToInt(ProductForCheck::getAmountProduct).sum();

        int clientDiscount = productCount >= 5 ?
                (client.getPersonalDiscount2() > 0 ? client.getPersonalDiscount2() : client.getPersonalDiscount1()):
                client.getPersonalDiscount1();

        productsForCheck
                .forEach(productForCheck -> {
                    int finalProductDiscount = productForCheck.getSumDiscount() + clientDiscount;
                    if(finalProductDiscount > 18)
                        finalProductDiscount = 18;

                    productForCheck.setSumDiscount(finalProductDiscount);
                });

        return productsForCheck
                .stream()
                .mapToDouble(productForCheck ->
                        priceWithDiscountAndAmount(
                                productForCheck.getProduct().getPrice(),
                                productForCheck.getSumDiscount(),
                                productForCheck.getAmountProduct()))
                .sum();
    }
}