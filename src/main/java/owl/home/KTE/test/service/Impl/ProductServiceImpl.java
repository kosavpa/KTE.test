package owl.home.KTE.test.service.Impl;
/**
 * Имплиментация серверного слоя товара, некоторые методы могут кидать непроверяемые исключения
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.repo.ProductRepository;
import owl.home.KTE.test.service.Interface.*;
import owl.home.KTE.test.service.util.Carrensy;

import java.util.*;

import static owl.home.KTE.test.service.util.ProductUtil.*;
import static owl.home.KTE.test.service.util.RatingUtil.distributionStarMap;
import static owl.home.KTE.test.service.util.RatingUtil.middleStar;


@Component
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private RatingService ratingService;
    private ClientService clientService;
    private ProductForCheckService productForCheckService;
    private CheckService checkService;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setProductForCheckService(ProductForCheckService productForCheckService) {
        this.productForCheckService = productForCheckService;
    }

    @Autowired
    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }

    /**
     * Ищет товар по идентификатору. Может бросить RuntimeException, если товара с таким id нет,
     * с соответствующим сообщением
     * @param productId - идентификатор товара
     * @return - товар
     */
    @Override
    public Product productById(long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product with this id not found!")
        );
    }

    @Transactional
    @Override
    public boolean deleteProductById(long productId) {
        productRepository.deleteById(productId);

        return !productRepository.existsById(productId);
    }

    @Transactional
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> allProduct() {
        return productRepository.findAll();
    }

    /**
     * Составляет информацию о товаре в которую входят - имя товара, описание товара, средняя оценка, клиент, что запросил информацию,
     * оценка клиента этого товара и распределение всех оценок товара. Может бросить RuntimeException, если id товара и клиента никак не соотносятся,
     * с соответствующим сообщением
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @return
     */
    @Transactional
    @Override
    public AdditionalProductInfo additionalProductInfo(long productId, long clientId){
        Product product = productById(productId);
        Client client = clientService.clientById(clientId);
        Rating rating = ratingService.findByProductIdAndClientId(productId, clientId).orElseThrow(
                () -> new IllegalArgumentException("Rating with this ratin id and client id not found!")
        );
        List<Rating> ratingsThisProduct = ratingService.findByProductId(productId);

        Map<Integer, Double> distributionStar = distributionStarMap(ratingsThisProduct);
        double middleStar = middleStar(distributionStar);

        return AdditionalProductInfo
                .builder()
                .productName(product.getName())
                .about(product.getAbout())
                .middleStar(middleStar)
                .client(client)
                .userRating(rating.getAmountStar())
                .starsDistribution(distributionStar)
                .build();
    }

    @Override
    public TotalPriceShopingListResponse totalPriceResponse(List<TotalPriceShopingListRequest> request) {
        Map<Long, Product> productMap = new HashMap<>();
        request
                .stream()
                .forEach(tpr -> productMap.put(tpr.getProductId(), productById(tpr.getProductId())));

        return totalPriceProductResponseFromRequestShopingList(request, Carrensy.KOP, productMap);
    }

    @Override
    public StatisticProductResponse statisticProduct(long productId) {
        Product product = productById(productId);
        int amountCheck = productForCheckService.countByproductId(productId);
        List<ProductForCheck> productForCheck = productForCheckService.productList(productId);
        double priceSum = totalPriceForStatisticFromShopingList(productForCheck);
        int discountSum = totalDiscountForStatisticFromShopingList(productForCheck);

        return StatisticProductResponse
                .builder()
                .product(product)
                .totalCostAtOriginalPrise(priceSum)
                .amountCheck(amountCheck)
                .discountSum(discountSum)
                .build();
    }
}