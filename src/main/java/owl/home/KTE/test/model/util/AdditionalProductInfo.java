package owl.home.KTE.test.model.util;
/**
 * Расширенная информация о продукте, которая связанна с клиентом и включае в себя:
 * название продукита, описание, среднюю оценку, клиента, оценку поставленную клиентом и распределение оценок
 */

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