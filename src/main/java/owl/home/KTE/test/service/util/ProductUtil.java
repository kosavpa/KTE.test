package owl.home.KTE.test.service.util;


import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.service.Interface.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


public class ProductUtil {
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
}