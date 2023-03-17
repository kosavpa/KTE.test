package owl.home.KTE.test.model.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @Builder
public class TotalPriceShopingListRequest {
    private long productId;
    private int amount;
}
