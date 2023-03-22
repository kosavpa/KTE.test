package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.check.Check;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static owl.home.KTE.test.service.util.ProductUtil.*;
import static owl.home.KTE.test.service.util.RatingUtil.distributionStarMap;
import static owl.home.KTE.test.service.util.RatingUtil.middleStar;


@Component
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RatingService ratingService;
    @Autowired
    ClientService clientService;
    @Autowired
    ProductForCheckService productForCheckService;
    @Autowired
    CheckService checkService;

    @Override
    public Product productById(long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product with this id not found!")
        );
    }

    @Override
    public boolean deleteProductById(long productId) {
        productRepository.deleteById(productId);

        return !productRepository.existsById(productId);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> allProduct() {
        return productRepository.findAll();
    }

    @Override
    public AdditionalProductInfo additionalProductInfo(long productId, long clientId){
        Product product = productById(productId);
        Client client = clientService.clientById(clientId);
        Rating rating = ratingService.findByProductIdAndClientId(productId, clientId).orElseThrow(
                () -> new IllegalArgumentException("Rating with this id not found!")
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
        return totalPriceProductResponseFromRequestShopingList(request, this);
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