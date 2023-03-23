package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.Date;
import java.util.Set;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class CheckForResponce {
    private String number;
    private Date date;
    private Set<ProductForCheck> shoppingList;
    private Client client;
    private double finalPrice;
}
