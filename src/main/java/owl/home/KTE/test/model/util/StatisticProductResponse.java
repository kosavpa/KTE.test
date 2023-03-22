package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.model.product.Product;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticProductResponse {
    private Product product;
    private int amountCheck;
    private double totalCostAtOriginalPrise;
    private int discountSum;
}
