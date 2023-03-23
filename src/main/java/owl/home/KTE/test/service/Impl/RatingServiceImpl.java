package owl.home.KTE.test.service.Impl;
/**
 * Имплиментация серверного слоя рейтинга, некоторые методы могут кидать непроверяемые исключения
 */

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

    /**
     * Ищет рейтинг по идентификатору. Может бросить RuntimeException, если рейтинга с таким id нет,
     * с соответствующим сообщением
     * @param ratingId - идентификатор рейтинга
     * @return - рейтинг или бросает IllegalArgumentException
     */
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

    /**
     * Сохраняет или удаляет оценку товара пользователем, может бросить RuntimeException
     * если операция удаления была провальна или оценка не корректна
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @param amountStar - отзыв о товаре (количество звезд), если 0 то "отзыв" оценки
     */
    @Transactional
    @Override
    public void saveFeedbackProduct(long productId, long clientId, int amountStar) {
        if(amountStar < 0 | amountStar > 5)
            throw new IllegalArgumentException("Bad feedback!");

        Product product = productService.productById(productId);
        Client client = clientService.clientById(clientId);
        Rating newRating = Rating
                .builder()
                .product(product)
                .client(client)
                .amountStar(amountStar)
                .build();

        Optional<Rating> byProductIdAndClientId = findByProductIdAndClientId(productId, clientId);

        if (byProductIdAndClientId.isPresent()){
            Rating oldRating = byProductIdAndClientId.get();

            if (amountStar == 0){
                if (deleteRatingById(oldRating.getId())){
                    return;
                } else {
                    throw new IllegalArgumentException("Product or client id is bad!");
                }
            }

            oldRating.setAmountStar(amountStar);
            saveRating(oldRating);
        } else {
            saveRating(newRating);
        }
    }

    /**
     * Создает объект AdditionalProductInfo, но с 0 рейтингом пользователя
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @return - AdditionalProductInfo
     */
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
