package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.repo.RatingRepository;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.Interface.RatingService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Component

public class RatingServiceImpl implements RatingService {
    @Autowired
    RatingRepository repository;
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;

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

    @Override
    public void saveFeedbackProduct(long productId, long clientId, int amountStar) {
        Product product = productService.productById(productId);
        Client client = clientService.clientById(clientId);
        Rating newRating = Rating
                .builder()
                .product(product)
                .client(client)
                .amountStar(amountStar)
                .build();

        Rating oldRating = findByProductIdAndClientId(productId, clientId).orElse(newRating);

        if (amountStar == 0){
            if(oldRating == newRating)
                return;

            deleteRatingById(oldRating.getId());
            return;
        }

        saveRating(oldRating);
    }
}
