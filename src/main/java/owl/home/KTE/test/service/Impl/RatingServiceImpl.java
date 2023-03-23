package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.repo.RatingRepository;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.Interface.RatingService;
import owl.home.KTE.test.service.util.RatingUtil;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


@Component
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {
    private RatingRepository repository;
    private ProductService productService;
    private ClientService clientService;

    @Autowired
    public void setRepository(RatingRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

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

    @Transactional
    @Override
    public boolean deleteRatingById(long ratingId) {
        repository.deleteRatingById(ratingId);

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

    @Transactional
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

    @Override
    public AdditionalProductInfo createEmptyAdditonalProductInfo(long productId, long clientId){
        Product product = productService.productById(productId);
        List<Rating> ratingByProductId = findByProductId(productId);
        Map<Integer, Double> distributionStarMap = RatingUtil.distributionStarMap(ratingByProductId);
        double middleStar = RatingUtil.middleStar(distributionStarMap);

        return AdditionalProductInfo
                .builder()
                .productName(product.getName())
                .about(product.getAbout())
                .client(clientService.clientById(clientId))
                .userRating(0)
                .middleStar(middleStar)
                .starsDistribution(distributionStarMap)
                .build();
    }
}
