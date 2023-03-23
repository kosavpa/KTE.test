package owl.home.KTE.test.model.util;
/**
 * Статистика продукта, включает в себя:
 * продукт, количество чеко в которых есть этот продукт,
 * итоговая стоимость по стоимости без скидок, сумму скидок
 */

import lombok.*;
import owl.home.KTE.test.model.product.Product;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticProductResponse {
    private Product product;
    private int amountCheck;
    private double totalCostAtOriginalPrise;
    private int discountSum;
}
