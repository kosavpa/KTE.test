package owl.home.KTE.test.webService.Impl;
/**
 * Имплиментция веб службы продуктов
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.*;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.Interface.RatingService;
import owl.home.KTE.test.webService.Interface.ProductWebService;

import javax.jws.WebService;
import java.util.List;


@Component
@WebService(serviceName = "ProductService", targetNamespace = "http://kte.test-web-service")
public class ProductWebServiceImpl implements ProductWebService {
    private ProductService productService;
    private CheckService checkService;
    private RatingService ratingService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public List<Product> getAllProduct() {
        return productService.allProduct();
    }

    @Override
    public AdditionalProductInfo getAdditionalProductInfo(long productId, long clientId) {
        return productService.additionalProductInfo(productId, clientId);
    }

    @Override
    public TotalPriceShopingListResponse totalPriceShopingLists(List<TotalPriceShopingListRequest> shopingList) {
        return productService.totalPriceResponse(shopingList);
    }

    @Override
    public AdditionalProductInfo feedBackProduct(long productId, long clientId, int amountStar) {
        ratingService.saveFeedbackProduct(productId, clientId, amountStar);

        return productService.additionalProductInfo(productId, clientId);
    }

    @Override
    public StatisticProductResponse productStatisctic(long productId) {
        return productService.statisticProduct(productId);
    }

    @Override
    public CheckForResponce generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList) {
        return checkService.generateCheck(clientId, totalPrice, shopingList);
    }
}