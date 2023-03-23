package owl.home.KTE.test.model.util;
/**
 * Клас для возврата информации о чеке, включает в себя номер чека, с лидирующими нулями, если
 * количество цифр в номере меньше 5, список продуктов в чеке, клиента и итоговую стоимость
 */

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
