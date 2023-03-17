package owl.home.KTE.test.model.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import owl.home.KTE.test.model.product.Product;


@Setter @Getter @Builder
public class StatisticProductResponse {
    private Product product;
    private int amountCheck;
    private double totalCostAtOriginalPrise;
    private int discountSum;
}
