package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.model.client.Client;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class StatisticClientResponse {
    private Client client;
    private int amountCheck;
    private double totalCheckSum;
    private int totalCheckDiscountSum;
}
