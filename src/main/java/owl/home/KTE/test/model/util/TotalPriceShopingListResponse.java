package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.service.util.Carrensy;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class TotalPriceShopingListResponse {
    private double totalPrice;
    private Carrensy carrensy;
}
