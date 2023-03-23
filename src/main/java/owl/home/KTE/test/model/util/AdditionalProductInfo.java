package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.model.client.Client;

import java.util.Map;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class AdditionalProductInfo {
    private String productName;
    private String about;
    private double middleStar;
    private Client client;
    private int userRating;
    private Map<Integer, Double> starsDistribution;
}