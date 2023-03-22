package owl.home.KTE.test.service.util;

import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.List;

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
}
