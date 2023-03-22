package owl.home.KTE.test.model.util;


import lombok.*;


@Getter @Setter @Builder @AllArgsConstructor@NoArgsConstructor
public class TotalPriceShopingListRequest {
    private long productId;
    private int amount;
}
