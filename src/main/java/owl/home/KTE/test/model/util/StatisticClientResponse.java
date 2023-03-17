package owl.home.KTE.test.model.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import owl.home.KTE.test.model.client.Client;


@Getter @Setter @Builder
public class StatisticClientResponse {
    private Client client;
    private int amountCheck;
    private double totalCheckSum;
    private int totalCheckDiscountSum;
}
