package owl.home.KTE.test.service.util;


import org.springframework.http.HttpRequest;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.service.Interface.ProductService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


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

    public static List<TotalPriceShopingListRequest> totalPriseRequestList(String[] paramsArray){
        List<TotalPriceShopingListRequest> totalPriseRequestList = new LinkedList<>();

        boolean revers = false;
        if(paramsArray[0].contains("amount"))
            revers = true;

        for(int i = 0; i < paramsArray.length; i++){
            if (revers){
                totalPriseRequestList.add(TotalPriceShopingListRequest
                        .builder()
                                .amount(Integer.parseInt(paramsArray[i]
                                        .split("=")[1]))
                                .productId(Long.parseLong(paramsArray[++i]
                                        .split("=")[1]))
                                .build());
            } else {
                totalPriseRequestList.add(TotalPriceShopingListRequest
                        .builder()
                        .productId(Long.parseLong(paramsArray[i]
                                .split("=")[1]))
                        .amount(Integer.parseInt(paramsArray[++i]
                                .split("=")[1]))
                        .build());
            }
        }

        return totalPriseRequestList;
    }

    public static String[] uriParamsArray(HttpRequest request){
        String[] uriParamsInRow = request.getURI().getPath().split("\\?");
        if (uriParamsInRow.length != 2)
            throw new IllegalArgumentException("Bad uri request!");

        String[] separatedUriParams = uriParamsInRow[1].split("&");
        if(separatedUriParams.length % 2 != 0)
            throw new IllegalArgumentException("Bad uri request, identificator product and amount must be sent in pairs!");

        return separatedUriParams;
    }

    public static double priceWithDiscountAndAmount(Product product, int productAmount){
        double productPrice = product.getPrice();
        int productDiscount = product.getDiscount();

        return ((productPrice - productPrice * productDiscount / 100) * productAmount);
    }

    public static TotalPriceShopingListResponse totalPriceProductResponseFromRequestList(
            List<TotalPriceShopingListRequest> requestList,
            ProductService service){

        return TotalPriceShopingListResponse
                .builder()
                .totalPrice(
                        requestList
                                .stream()
                                .flatMapToDouble(tppr ->
                                        DoubleStream.of(
                                                priceWithDiscountAndAmount(
                                                        service.productById(tppr.getProductId()),
                                                        tppr.getAmount())))
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
}