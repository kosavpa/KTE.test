package owl.home.KTE.test.model.util;


import lombok.*;
import owl.home.KTE.test.model.client.Client;

import java.util.Map;


@Getter @Setter @Builder
public class AdditionalProductInfo {
    private String about;
    private double middleStar;
    private Client client;
    private int userRating;
    private Map<Integer, Double> starsDistribution;
}