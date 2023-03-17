package owl.home.KTE.test.service.Interface;


import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.product.Rating;

import java.util.List;
import java.util.Optional;


@Service
public interface RatingService {
    Rating getRatingById(long ratingId);
    Rating saveRating(Rating rating);
    boolean deleteRatingById(long id);
    List<Rating> findByProductId(long productId);
    Optional<Rating> findByProductIdAndClientId(long productId, long clientId);
}
