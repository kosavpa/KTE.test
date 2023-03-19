package owl.home.KTE.test.service.util;


import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.service.Interface.ProductService;
import test_web_service.kte.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static owl.home.KTE.test.service.util.ClientUtil.*;


public class ProductUtil {
    public static Map<Integer, Double> distributionStarMap(List<Rating> ratingsListProduct){
        Map<Integer, Double> result = new HashMap<>();

        ratingsListProduct.stream().forEach(rating -> {
            Integer amountStar = rating.getAmountStar();
            Double currentAmountStarInMap = result.get(amountStar);

            if(currentAmountStarInMap == null){
                currentAmountStarInMap = 1.0d;
                result.put(amountStar, currentAmountStarInMap);
            } else {
                result.put(amountStar, currentAmountStarInMap + 1.0d);
            }
        });

        return result;
    }

    public static double middleStar(Map<Integer, Double> distributionStar){
        return distributionStar.entrySet().stream().flatMapToDouble(e -> DoubleStream.of(e.getValue())).average().orElse(0.0d);
    }

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

    public static double priceWithDiscountAndAmount(Product product, int productAmount){
        double productPrice = product.getPrice();
        int productDiscount = product.getDiscount();

        return ((productPrice - productPrice * productDiscount / 100) * productAmount);
    }

    public static TotalPriceShopingListResponse totalPriceProductResponseFromRequestShopingList(
            List<TotalPriceShopingListRequest> requestList,
            ProductService service){

        return TotalPriceShopingListResponse
                .builder()
                .totalPrice(
                        requestList
                                .stream()
                                .flatMapToDouble(tpr ->
                                        DoubleStream.of(
                                                priceWithDiscountAndAmount(
                                                        service.productById(tpr.getProductId()),
                                                        tpr.getAmount())))
//                                В копейках
                                .sum() * 100)
                .carrensy(Carrensy.RUB)
                .build();
    }

    public static double totalPriceForStatisticFromShopingList(List<ProductForCheck> productForCheck){
        return productForCheck
                .stream()
                .mapToDouble(sl -> sl.getProduct().getPrice())
                .sum();
    }

    public static int totalDiscountForStatisticFromShopingList(List<ProductForCheck> productForCheck){
        return productForCheck
                .stream()
                .mapToInt(ProductForCheck::getSumDiscount)
                .sum();
    }

    public static Set<ProductForCheck> productForCheckListFromTotalPriceShopingListRequest(
            List<TotalPriceShopingListRequest> shopingList,
            ProductService service){
        return shopingList
                .stream()
                .map(tpslr -> {
                    Product product = service.productById(tpslr.getProductId());
                    return ProductForCheck
                            .builder()
                            .product(product)
                            .amountProduct(tpslr.getAmount())
                            .sumDiscount(product.getDiscount())
                            .build();
                })
                .collect(Collectors.toSet());
    }

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
                                productForCheck.getProduct(),
                                productForCheck.getAmountProduct()))
                .sum();
    }

    public static List<test_web_service.kte.Product> mapProductListToWsProductList(List<Product> products){
        return products
                .stream()
                .map(product -> {
                    test_web_service.kte.Product wsProduct = new test_web_service.kte.Product();

                    wsProduct.setId(product.getId());
                    wsProduct.setName(product.getName());
                    wsProduct.setAbout(product.getAbout());
                    wsProduct.setPrice(product.getPrice());
                    wsProduct.setDiscount(product.getDiscount());

                    return wsProduct;})
                .collect(Collectors.toList());
    }

    public static test_web_service.kte.AdditionalProductInfo mapAdditionalProductInfoToWsAdditionalProductInfo(AdditionalProductInfo productInfo){
        test_web_service.kte.AdditionalProductInfo wsProductInfo = new test_web_service.kte.AdditionalProductInfo();
        test_web_service.kte.Map distributionStar = new test_web_service.kte.Map();

        distributionStar.getMapEntry().addAll(mapMapEntryToWsMapEntry(productInfo.getStarsDistribution().entrySet()));
        wsProductInfo.setAbout(productInfo.getAbout());
        wsProductInfo.setProductName(productInfo.getProductName());
        wsProductInfo.setClient(mapClientToWsClient(productInfo.getClient()));
        wsProductInfo.setMiddleStar(productInfo.getMiddleStar());
        wsProductInfo.setUserRating(productInfo.getUserRating());
        wsProductInfo.setStarsDistribution(distributionStar);

        return wsProductInfo;
    }

    public static Collection<? extends MapEntry> mapMapEntryToWsMapEntry(Set<Map.Entry<Integer, Double>> entrySet) {
        return entrySet
                .stream()
                .map(entry -> {
                    MapEntry wsMapEntry = new MapEntry();

                    wsMapEntry.setKey(entry.getKey());
                    wsMapEntry.setValue(entry.getValue());

                    return wsMapEntry;})
                .collect(Collectors.toList());
    }

    public static List<TotalPriceShopingListRequest> mapWsRequestTototalPriceShopingListRequests( List<TotalPriseRequest> wsRequest){
        return wsRequest
                .stream()
                .map(wsReq -> {
                    return TotalPriceShopingListRequest
                            .builder()
                            .productId(wsReq.getProductId())
                            .amount(wsReq.getAmount())
                            .build();})
                .collect(Collectors.toList());
    }

    public static TotalPriceResponse mapTotalPriceShopingListToWsTotalPrise(TotalPriceShopingListResponse totalPriceShopingListResponse){
        TotalPriceResponse wsResp = new TotalPriceResponse();
        test_web_service.kte.Carrensy carrensy = new test_web_service.kte.Carrensy();
        carrensy.setCarrensyVal(totalPriceShopingListResponse.getCarrensy().name);
        wsResp.setCarrency(carrensy);
        wsResp.setTotalPrice(totalPriceShopingListResponse.getTotalPrice());

        return wsResp;
    }

    public static StatisticProduct mapStatisticProductToWsStatisticProduct(StatisticProductResponse statisticProductResponse) {
        StatisticProduct wsStatistic = new StatisticProduct();

        wsStatistic.setProduct(mapProductToWsProduct(statisticProductResponse.getProduct()));
        wsStatistic.setAmountCheck(statisticProductResponse.getAmountCheck());
        wsStatistic.setDiscountSum(statisticProductResponse.getDiscountSum());
        wsStatistic.setTotalCostAtOriginalPrise(statisticProductResponse.getTotalCostAtOriginalPrise());

        return wsStatistic;
    }

    public static test_web_service.kte.Product mapProductToWsProduct(Product product) {
        test_web_service.kte.Product wsProduct = new test_web_service.kte.Product();

        wsProduct.setDiscount(product.getDiscount());
        wsProduct.setId(product.getId());
        wsProduct.setName(product.getName());
        wsProduct.setAbout(product.getAbout());
        wsProduct.setPrice(product.getPrice());

        return wsProduct;
    }

    public static test_web_service.kte.Check mapCheckToWsCheck(Check check) {
        test_web_service.kte.Check wsCheck = new test_web_service.kte.Check();

        ShopingList shopingList = new ShopingList();
        shopingList.getShopingListElement().addAll(mapProductForCheckListToWsShopingList(check.getShoppingList()));
        wsCheck.setClient(mapClientToWsClient(check.getClient()));
        wsCheck.setFinalPrice(check.getFinalPrice());
        wsCheck.setNumber(check.getNumber());
        wsCheck.setDate(mapCalendarToXmlGregorianColendar(Calendar.getInstance().getTime()));
        wsCheck.setShopingList(shopingList);

        return wsCheck;
    }

    public static List<ShopingListElement> mapProductForCheckListToWsShopingList(Set<ProductForCheck> shoppingList) {
        return shoppingList
                .stream()
                .map(productForCheck -> {
                    ShopingListElement wsElem = new ShopingListElement();

                    wsElem.setProduct(mapProductToWsProduct(productForCheck.getProduct()));
                    wsElem.setId(productForCheck.getId());
                    wsElem.setSumDiscount(productForCheck.getSumDiscount());
                    wsElem.setAmountProduct(productForCheck.getAmountProduct());

                    return wsElem;})
                .collect(Collectors.toList());
    }

    public static String mapCalendarToXmlGregorianColendar(Date time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(time);
    }
}