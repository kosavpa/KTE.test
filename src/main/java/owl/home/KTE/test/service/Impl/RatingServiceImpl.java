package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.repo.RatingRepository;
import owl.home.KTE.test.service.Interface.RatingService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Component

public class RatingServiceImpl implements RatingService {
    @Autowired
    RatingRepository repository;

    @Override
    public Rating getRatingById(long ratingId) {
        return repository.findById(ratingId).orElseThrow(
                () -> new IllegalArgumentException("Rating with this id not found!")
        );
    }

    @Override
    public Rating saveRating(Rating rating) {
        return repository.save(rating);
    }

    @Override
    public boolean deleteRatingById(long ratingId) {
        repository.deleteById(ratingId);

        return !repository.existsById(ratingId);
    }

    @Override
    public List<Rating> findByProductId(long productId) {
        return repository.findByProductId(productId);
    }

    @Override
    public Optional<Rating> findByProductIdAndClientId(long productId, long clientId) {
        return repository.findByProductIdAndClientId(productId, clientId);
    }
}
