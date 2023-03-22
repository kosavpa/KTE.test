package owl.home.KTE.test.service.util;

import owl.home.KTE.test.model.product.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingUtil {
    public static Map<Integer, Double> distributionStarMap(List<Rating> ratingsListProduct){
        Map<Integer, Double> result = new HashMap<>();

        ratingsListProduct.stream().forEach(rating -> {
            Integer amountStar = rating.getAmountStar();
            Double currentAmountStarInMap = result.get(amountStar);

            if(currentAmountStarInMap == null){
                currentAmountStarInMap = 1.0d;
                result.put(amountStar, currentAmountStarInMap);
            } else {
                result.put(amountStar, currentAmountStarInMap + 1.0d);
            }
        });

        return result;
    }

    public static double middleStar(Map<Integer, Double> distributionStar){
        return distributionStar.entrySet().stream().mapToDouble(e -> (e.getValue() * e.getKey())).average().orElse(0.0d);
    }
}
