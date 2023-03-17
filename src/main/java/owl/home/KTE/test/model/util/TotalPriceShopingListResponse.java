package owl.home.KTE.test.model.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import owl.home.KTE.test.service.util.Carrensy;

@Getter @Setter @Builder
public class TotalPriceShopingListResponse {
    private double totalPrice;
    private Carrensy carrensy;
}
