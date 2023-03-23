package owl.home.KTE.test.model.util;
/**
 * Класс статистики о клиенте включает в себя:
 * клиента, количество чеков, сумму стоимости всех чеков, и сумма всех скидок
 */

import lombok.*;
import owl.home.KTE.test.model.client.Client;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticClientResponse {
    private Client client;
    private int amountCheck;
    private double totalCheckSum;
    private int totalCheckDiscountSum;
}
