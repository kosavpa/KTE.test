package owl.home.KTE.test.service.util;
/**
 * Утильный класс для клиентов
 */

import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.List;


public class ClientUtil {
    /**
     * Стоимость всех чеков оформленых на клиента
     * @param checkList - идентификатор клиента
     * @return - стоимость всех чеков
     */
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

    /**
     * Итоговая скидка клиента по всем чекам
     * @param checkList - список чеков
     * @return - сумма скидок
     */
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
